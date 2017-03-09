package com.elliot.clicker.controller

import java.util.concurrent.atomic.AtomicBoolean

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import com.elliot.clicker.actor.entity.{CurrentWarpData, GetValue, _}
import com.elliot.clicker.actor.{GameStatisticsActor, GameValuesActor}
import com.elliot.clicker.entity.GameValues
import com.elliot.clicker.jni.JNIAdapter
import org.joda.time.{DateTime, DateTimeZone}
import org.joda.time.format.DateTimeFormat

import scalafx.event.ActionEvent
import scalafx.scene.control.{Button, TextArea, TextField}
import scalafxml.core.macros.sfxml
import akka.pattern._
import com.elliot.clicker.constant.GameConstants
import com.elliot.clicker.manager.GameSequenceManager

import scala.collection.mutable.ArrayBuffer
import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scalafx.application.Platform

@sfxml
class MainWindowController(private val startButton: Button,
                           private val stopButton: Button,
                           private val resetButton: Button,
                           private val levelText: TextField,
                           private val warpCubesText: TextField,
                           private val last1MinCubesAvgText: TextField,
                           private val last1MinCubesText: TextField,
                           private val last5MinCubesAvgText: TextField,
                           private val last5MinCubesText: TextField,
                           private val last60MinCubesAvgText: TextField,
                           private val last60MinCubesText: TextField,
                           private val runTimeText: TextField,
                           private val forceWarpButton: Button,
                           private val logTextArea: TextArea,
                           private val ignoreWarpText: TextField) {

  var updateThread = null;
  implicit val timeout: Timeout = Timeout(5, java.util.concurrent.TimeUnit.SECONDS)

  val system = ActorSystem("GameValuesActor")
  val dataActor = system.actorOf(Props(GameValuesActor), "GameValuesActor")

  val statistics1minActor = system.actorOf(Props(new GameStatisticsActor(1, dataActor)), "GameStatistics1Actor")
  val statistics5minActor = system.actorOf(Props(new GameStatisticsActor(5, dataActor)), "GameStatistics2Actor")
  val statistics60minActor = system.actorOf(Props(new GameStatisticsActor(60, dataActor)), "GameStatistics3Actor")

  val running: AtomicBoolean = new AtomicBoolean(false)

  val format = DateTimeFormat.forPattern("HH:mm:ss")

  def forceWarpClick(event: ActionEvent): Unit = {
    running.set(false)

    val adapter: JNIAdapter = new JNIAdapter()
    new Thread {
      override def run: Unit = {
        Thread.sleep(5000)
        resetOnClick(event)
        GameSequenceManager.executeTimeWarp(adapter)
        startOnClick(event)
      }
    }.start()
  }

  def stopOnClick(event: ActionEvent): Unit = {
    running.set(false)
    resetOnClick(event)
    Platform.runLater {
      startButton.setDisable(false)
      stopButton.setDisable(true)
      forceWarpButton.setDisable(true)
    }
  }

  def resetOnClick(event: ActionEvent): Unit = {
    dataActor ! Reset
    dataActor ! new SetStartTime
    val adapter: JNIAdapter = new JNIAdapter()
    updateBoard(adapter)
  }

  def startOnClick(event: ActionEvent) {
    running.set(true)
    resetOnClick(event)
    updateBoard()
    controlGame()
    warpPlanner()
    Platform.runLater {
      startButton.setDisable(true)
      stopButton.setDisable(false)
      forceWarpButton.setDisable(false)
    }
  }

  def warpPlanner(): Unit = {
    val adapter: JNIAdapter = new JNIAdapter()
    new Thread {
      override def run {
        while (running.get()) {

          val warpData = dataActor ? CurrentWarpData
          val anwser1m = statistics1minActor ? GetStatistics
          val anwser5m = statistics5minActor ? GetStatistics
          val anwser60m = statistics60minActor ? GetStatistics

          val values = for {
            wd <- warpData
            f1Result <- anwser1m
            f2Result <- anwser5m
            f3Result <- anwser60m
          } yield (f1Result, f2Result, f3Result, wd)

          values.onComplete {
            case Success((v1: GameStatistics, v5: GameStatistics, v60: GameStatistics, wdValues: CurrentWarpData)) => {


              if (((System.currentTimeMillis() - wdValues.startTime) > (ignoreWarpText.getText.toInt * 60 * 1000)) && (v1.average < v5.average) && (v5.average < v60.average) && (v5.average < (v60.average * .6))) {

                val currentData = dataActor ? GetValue

                currentData.onComplete {
                  case Success( cdValues: GameValues) => {
                    logTextArea.appendText("------ Time Warp ------ \n")
                    logTextArea.appendText("Run duration: " + format.print((new DateTime(System.currentTimeMillis(), DateTimeZone.UTC)).minus(wdValues.startTime)) + " \n")
                    logTextArea.appendText("Start level: " + wdValues.startLevel.toInt + " End level: " + cdValues.level.toInt + " Level climbed: " + (cdValues.level - wdValues.startLevel).toInt + "\n")
                    logTextArea.appendText("Gained time cubes: " + (cdValues.currentWarpTimeCubes - wdValues.startTimeCubes).toInt + "\n")
                    logTextArea.appendText("AVG TC/min last 5 min: " + v5.average.toInt + " \n")
                    logTextArea.appendText(" AVG TC/min last 60 min: " + v60.average.toInt + " \n")

                    forceWarpClick(null)
                  }
                }

              }
            }
          }


          Thread.sleep(1000)
        }
      }
    }.start()
  }

  def controlGame(): Unit = {
    val adapter: JNIAdapter = new JNIAdapter()
    val time: Long = System.currentTimeMillis()
    new Thread {
      override def run {
        while (running.get()) {
          if ((System.currentTimeMillis() - time) / 1000 % 10 == 0) {
            GameSequenceManager.executeTeamLevelUp(adapter,1)
            GameSequenceManager.executeUseSpells(adapter)
          } else {
            GameSequenceManager.executeAttack(adapter)
          }
          Thread.sleep(50)
        }
      }
    }.start()
  }

  def updateBoard(): Unit = {
    val adapter: JNIAdapter = new JNIAdapter()
    new Thread {
      override def run {
        while (running.get()) {
          dataActor ! adapter.getGameValues()

          updateBoard(adapter)

          Thread.sleep(500)
        }
      }
    }.start()

  }

  private def updateBoard(adapter: JNIAdapter): Unit = {

    val startTime = dataActor ? GetStartTime
    val currentValues = dataActor ? GetValue
    val anwser1m = statistics1minActor ? GetStatistics
    val anwser5m = statistics5minActor ? GetStatistics
    val anwser60m = statistics60minActor ? GetStatistics

    val values = for {
      f1Result <- anwser1m
      f2Result <- anwser5m
      f3Result <- anwser60m
      f4Result <- startTime
      f5Result <- currentValues
    } yield (f1Result, f2Result, f3Result, f4Result, f5Result)

    values.onComplete {
      case Success((v1: GameStatistics, v5: GameStatistics, v60: GameStatistics, startTime: Long, v0: GameValues)) => {
        Platform.runLater {

          val currentTime = new DateTime(System.currentTimeMillis(), DateTimeZone.UTC)
          runTimeText.text = format.print(currentTime.minus(startTime))

          levelText.text = v0.level.toInt.toString
          warpCubesText.text = v0.currentWarpTimeCubes.toInt.toString
          last1MinCubesAvgText.text = v1.average.toInt.toString
          last1MinCubesText.text = v1.count.toInt.toString
          last5MinCubesAvgText.text = v5.average.toInt.toString
          last5MinCubesText.text = v5.count.toInt.toString
          last60MinCubesAvgText.text = v60.average.toInt.toString
          last60MinCubesText.text = v60.count.toInt.toString
        }
      }
      case Failure(ex) =>
        ex.printStackTrace()
    }

  }

}