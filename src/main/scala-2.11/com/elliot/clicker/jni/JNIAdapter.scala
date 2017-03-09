package com.elliot.clicker.jni

import com.elliot.clicker.entity.GameValues

class JNIAdapter {
   @native def sendKeysToWindow(message: String): Unit
   @native def getGameValues(): GameValues
   @native def sendClicksToWindowByButtonAndPositionAndAmountAndDelay(button:Int, x:Int, y:Int, amountOfClick:Int, delayBetweenClick:Long)
}