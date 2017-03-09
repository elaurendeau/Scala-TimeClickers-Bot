package com.elliot.clicker.manager

import com.elliot.clicker.constant.GameConstants
import com.elliot.clicker.jni.JNIAdapter

/**
  * Created by Elliot Laurendeau on 2016-06-12.
  */
object GameSequenceManager {
  def executeTimeWarp(adapter: JNIAdapter): Unit = {
    adapter.sendClicksToWindowByButtonAndPositionAndAmountAndDelay(GameConstants.CODE_KEY_LEFT_MOUSE, GameConstants.POSITION_WARP_CLICK.x, GameConstants.POSITION_WARP_CLICK.y, GameConstants.VALUE_CLICK_AMOUNT_DEFAULT, GameConstants.VALUE_CLICK_TIMEOUT_DEFAULT)
    Thread.sleep(2000)
    adapter.sendClicksToWindowByButtonAndPositionAndAmountAndDelay(GameConstants.CODE_KEY_LEFT_MOUSE, GameConstants.POSITION_WARP_OK_CLICK.x, GameConstants.POSITION_WARP_OK_CLICK.y, GameConstants.VALUE_CLICK_AMOUNT_DEFAULT, GameConstants.VALUE_CLICK_TIMEOUT_DEFAULT)
    Thread.sleep(2000)
    adapter.sendClicksToWindowByButtonAndPositionAndAmountAndDelay(GameConstants.CODE_KEY_LEFT_MOUSE, GameConstants.POSITION_WARP_NEW_TIMELINE_CLICK.x, GameConstants.POSITION_WARP_NEW_TIMELINE_CLICK.y, GameConstants.VALUE_CLICK_AMOUNT_DEFAULT, GameConstants.VALUE_CLICK_TIMEOUT_DEFAULT)
    Thread.sleep(10000)
    executeLevelBaseSpellAndPistol(adapter)
    Thread.sleep(2000)
    executeTeamLevelUp(adapter, 10)
    Thread.sleep(1000)
    executeEnableIdleMode(adapter)
    Thread.sleep(500)
    executeUseSpells(adapter)

  }

  def executeTeamLevelUp(adapter: JNIAdapter, repeat: Int = 2): Unit = {
    for (u <- 0 to repeat) {
      adapter.sendClicksToWindowByButtonAndPositionAndAmountAndDelay(GameConstants.CODE_KEY_LEFT_MOUSE, GameConstants.POSITION_TEAM1.x, GameConstants.POSITION_TEAM1.y, GameConstants.VALUE_CLICK_AMOUNT_DEFAULT, GameConstants.VALUE_CLICK_TIMEOUT_DEFAULT)
      Thread.sleep(10)
      adapter.sendClicksToWindowByButtonAndPositionAndAmountAndDelay(GameConstants.CODE_KEY_LEFT_MOUSE, GameConstants.POSITION_TEAM2.x, GameConstants.POSITION_TEAM2.y, GameConstants.VALUE_CLICK_AMOUNT_DEFAULT, GameConstants.VALUE_CLICK_TIMEOUT_DEFAULT)
      Thread.sleep(10)
      adapter.sendClicksToWindowByButtonAndPositionAndAmountAndDelay(GameConstants.CODE_KEY_LEFT_MOUSE, GameConstants.POSITION_TEAM3.x, GameConstants.POSITION_TEAM3.y, GameConstants.VALUE_CLICK_AMOUNT_DEFAULT, GameConstants.VALUE_CLICK_TIMEOUT_DEFAULT)
      Thread.sleep(10)
      adapter.sendClicksToWindowByButtonAndPositionAndAmountAndDelay(GameConstants.CODE_KEY_LEFT_MOUSE, GameConstants.POSITION_TEAM4.x, GameConstants.POSITION_TEAM4.y, GameConstants.VALUE_CLICK_AMOUNT_DEFAULT, GameConstants.VALUE_CLICK_TIMEOUT_DEFAULT)
      Thread.sleep(10)
      adapter.sendClicksToWindowByButtonAndPositionAndAmountAndDelay(GameConstants.CODE_KEY_LEFT_MOUSE, GameConstants.POSITION_TEAM5.x, GameConstants.POSITION_TEAM5.y, GameConstants.VALUE_CLICK_AMOUNT_DEFAULT, GameConstants.VALUE_CLICK_TIMEOUT_DEFAULT)
    }
  }

  def executeLevelBaseSpellAndPistol(adapter: JNIAdapter): Unit = {
    adapter.sendClicksToWindowByButtonAndPositionAndAmountAndDelay(GameConstants.CODE_KEY_LEFT_MOUSE, GameConstants.POSITION_LEVEL_PISTOL_CLICK.x, GameConstants.POSITION_LEVEL_PISTOL_CLICK.y, GameConstants.VALUE_CLICK_AMOUNT_PISTOL_LEVEL, GameConstants.VALUE_CLICK_TIMEOUT_PISTOL_LEVEL)
    Thread.sleep(100)
    adapter.sendClicksToWindowByButtonAndPositionAndAmountAndDelay(GameConstants.CODE_KEY_LEFT_MOUSE, GameConstants.POSITION_LEVEL_SPELL_CLICK.x, GameConstants.POSITION_LEVEL_SPELL_CLICK.y, GameConstants.VALUE_CLICK_AMOUNT_SPELL_LEVEL, GameConstants.VALUE_CLICK_TIMEOUT_SPELL_LEVEL)
  }

  def executeTeamLevelUpByKeys(adapter: JNIAdapter): Unit = {
    adapter.sendKeysToWindow("ASDFGASDFG")
  }

  def executeEnableIdleMode(adapter: JNIAdapter): Unit = {
    adapter.sendClicksToWindowByButtonAndPositionAndAmountAndDelay(GameConstants.CODE_KEY_LEFT_MOUSE, GameConstants.POSITION_IDLE_MODE_GUN1.x, GameConstants.POSITION_IDLE_MODE_GUN1.y, GameConstants.VALUE_CLICK_AMOUNT_DEFAULT, GameConstants.VALUE_CLICK_TIMEOUT_DEFAULT)
    Thread.sleep(250)
    adapter.sendClicksToWindowByButtonAndPositionAndAmountAndDelay(GameConstants.CODE_KEY_LEFT_MOUSE, GameConstants.POSITION_IDLE_MODE_GUN2.x, GameConstants.POSITION_IDLE_MODE_GUN2.y, GameConstants.VALUE_CLICK_AMOUNT_DEFAULT, GameConstants.VALUE_CLICK_TIMEOUT_DEFAULT)
    Thread.sleep(250)
    adapter.sendClicksToWindowByButtonAndPositionAndAmountAndDelay(GameConstants.CODE_KEY_LEFT_MOUSE, GameConstants.POSITION_IDLE_MODE_GUN3.x, GameConstants.POSITION_IDLE_MODE_GUN3.y, GameConstants.VALUE_CLICK_AMOUNT_DEFAULT, GameConstants.VALUE_CLICK_TIMEOUT_DEFAULT)
  }

  def executeUseSpells(adapter: JNIAdapter): Unit = {
    adapter.sendClicksToWindowByButtonAndPositionAndAmountAndDelay(GameConstants.CODE_KEY_LEFT_MOUSE, GameConstants.POSITION_ACTIVATE_DIMENSION_SHIFT.x, GameConstants.POSITION_ACTIVATE_DIMENSION_SHIFT.y, GameConstants.VALUE_CLICK_AMOUNT_DEFAULT, GameConstants.VALUE_CLICK_TIMEOUT_DEFAULT)
    Thread.sleep(10)
    adapter.sendClicksToWindowByButtonAndPositionAndAmountAndDelay(GameConstants.CODE_KEY_LEFT_MOUSE, GameConstants.POSITION_ACTIVATE_ALL_SPELL.x, GameConstants.POSITION_ACTIVATE_ALL_SPELL.y, GameConstants.VALUE_CLICK_AMOUNT_DEFAULT, GameConstants.VALUE_CLICK_TIMEOUT_DEFAULT)
    Thread.sleep(10)
    adapter.sendClicksToWindowByButtonAndPositionAndAmountAndDelay(GameConstants.CODE_KEY_LEFT_MOUSE, GameConstants.POSITION_ACTIVATE_COOLDOWN.x, GameConstants.POSITION_ACTIVATE_COOLDOWN.y, GameConstants.VALUE_CLICK_AMOUNT_DEFAULT, GameConstants.VALUE_CLICK_TIMEOUT_DEFAULT)

  }

  def executeAttack(adapter: JNIAdapter): Unit = {
    adapter.sendClicksToWindowByButtonAndPositionAndAmountAndDelay(GameConstants.CODE_KEY_LEFT_MOUSE, GameConstants.POSITION_STANDARD_CLICK.x, GameConstants.POSITION_STANDARD_CLICK.y, GameConstants.VALUE_CLICK_ATTACK, GameConstants.VALUE_CLICK_TIMEOUT_ATTACK)
  }

}
