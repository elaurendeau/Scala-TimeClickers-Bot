package com.elliot.clicker.actor

import java.util.concurrent.TimeUnit

import akka.actor.{Actor, ActorRef}
import akka.actor.Actor.Receive
import com.elliot.clicker.actor.entity._
import com.elliot.clicker.entity.GameValues

import scala.collection.mutable.ArrayBuffer

/**
  * Created by Elliot Laurendeau on 2016-06-10.
  */
object GameValuesActor extends Actor{
  private val values:ArrayBuffer[GameValues] = ArrayBuffer()
  private var current:GameValues = new GameValues()
  private var startMillisSeconds: Long = System.currentTimeMillis();
  private var startLevel:Double = 0;
  private var startTimeCubes:Double = 0;

  override def receive: Receive = {
    case value:GameValues =>
      // ignore the timecubes because you can start at 0
      if(startLevel == 0) {
        startLevel = value.level
        startTimeCubes = value.currentWarpTimeCubes
      }

      values --= values.filterNot(_.creationTime > (System.currentTimeMillis() - (60 * 60 * 1000)))
      value.creationTime = System.currentTimeMillis()

      current = value
      values += value
    case GetStatistics =>
      sender ! values.clone()
    case GetValue =>
      sender ! current
    case GetStartTime =>
      sender ! startMillisSeconds
    case value: SetStartTime =>
      startMillisSeconds = value.timeMillisSeconds
    case CurrentWarpData =>
      sender ! new CurrentWarpData(startMillisSeconds, startLevel, startTimeCubes)
    case Reset =>
      startMillisSeconds = 0
      startLevel = 0
      startTimeCubes = 0
      values.clear()
      current = new GameValues()
  }
}
