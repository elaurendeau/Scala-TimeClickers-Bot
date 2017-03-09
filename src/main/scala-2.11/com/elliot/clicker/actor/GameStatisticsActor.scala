package com.elliot.clicker.actor

import akka.actor.{Actor, ActorRef}
import akka.pattern._
import akka.util.Timeout
import com.elliot.clicker.actor.entity.{GameStatistics, GetStatistics, GetValue}
import com.elliot.clicker.entity.GameValues

import scala.collection.mutable.ArrayBuffer
import scala.concurrent.Future
import scala.util.{Failure, Success}


/**
  * Created by Elliot Laurendeau on 2016-06-11.
  */
class GameStatisticsActor(val time: Int, val other: ActorRef) extends Actor {
  implicit val timeout: Timeout = Timeout(1, java.util.concurrent.TimeUnit.SECONDS)
  implicit val ec = context.system.dispatcher

  override def receive: Receive = {
    case GetStatistics => {
      val currentSender = sender
      val value = other ? GetStatistics
      value.onComplete {
        case Success(values: ArrayBuffer[GameValues]) =>
          Future {
            try {
              val stats: GameStatistics = new GameStatistics(warCubesLastXTime(values, time), warpCubesAvgPerMinLastXTime(values, time))
              currentSender ! stats
            } catch {
              case e: Exception => e.printStackTrace()
            }
          }
        case Failure(ex) =>
          ex.printStackTrace()
      }

    }
  }

  def warCubesLastXTime(values: ArrayBuffer[GameValues], time: Int): Double = {
    val list = values.filter(_.creationTime > System.currentTimeMillis() - (time * 60 * 1000))
    if(list == null || list.length == 0) {
      return 0
    } else {
      list.map(_.currentWarpTimeCubes).max - list.map(_.currentWarpTimeCubes).min
    }

  }

  def warpCubesAvgPerMinLastXTime(values: ArrayBuffer[GameValues], time: Int): Double = {
    val list = values.filter(_.creationTime > System.currentTimeMillis() - (time * 60 * 1000))

    if (list == null || list.length == 0 || list.map(_.currentWarpTimeCubes).max - list.map(_.currentWarpTimeCubes).min == 0) {
      return 0
    }

    val cubesAmount = warCubesLastXTime(values, time)
    val timeDelta = list.map(_.creationTime).max - list.map(_.creationTime).min

    (cubesAmount * 60) / ( timeDelta / 1000 )
  }
}
