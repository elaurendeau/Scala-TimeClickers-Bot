package com.elliot.clicker.constant

import com.elliot.clicker.entity.Point


object GameConstants {
  val CODE_KEY_LEFT_MOUSE:Int = 0x0001
  val CODE_KEY_RIGHT_MOUSE:Int = 0x0002
  val POSITION_STANDARD_CLICK:Point = new Point(825,175)

  val POSITION_LEVEL_SPELL_CLICK:Point = new Point(1225,325)
  val POSITION_LEVEL_PISTOL_CLICK:Point = new Point(1225,225)
  val POSITION_WARP_CLICK:Point = new Point(1225,325)
  val POSITION_WARP_OK_CLICK:Point = new Point(550,525)
  val POSITION_WARP_NEW_TIMELINE_CLICK:Point = new Point(1150,350)

  val POSITION_IDLE_MODE_GUN1:Point = new Point(390, 690)
  val POSITION_IDLE_MODE_GUN2:Point = new Point(895, 690)
  val POSITION_IDLE_MODE_GUN3:Point = new Point(580, 690)

  val POSITION_TEAM1:Point = new Point(60, 225)
  val POSITION_TEAM2:Point = new Point(60, 325)
  val POSITION_TEAM3:Point = new Point(60, 425)
  val POSITION_TEAM4:Point = new Point(60, 525)
  val POSITION_TEAM5:Point = new Point(60, 625)

  val POSITION_ACTIVATE_ALL_SPELL:Point = new Point(1215, 510)
  val POSITION_ACTIVATE_DIMENSION_SHIFT:Point = new Point(1075, 450)
  val POSITION_ACTIVATE_COOLDOWN:Point = new Point(1250, 450)

  val VALUE_CLICK_AMOUNT_DEFAULT:Int = 1;
  val VALUE_CLICK_TIMEOUT_DEFAULT:Int = 10;

  val VALUE_CLICK_AMOUNT_PISTOL_LEVEL:Int = 13;
  val VALUE_CLICK_TIMEOUT_PISTOL_LEVEL:Int = 50;

  val VALUE_CLICK_AMOUNT_SPELL_LEVEL:Int = 10;
  val VALUE_CLICK_TIMEOUT_SPELL_LEVEL:Int = 50;

  val VALUE_CLICK_ATTACK:Int = 14;
  val VALUE_CLICK_TIMEOUT_ATTACK:Int = 2;
}