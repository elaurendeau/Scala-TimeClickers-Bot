package com.elliot.clicker.entity

class GameValues(val level: Double = 0,
                 val currentWarpTimeCubes: Double = 0,
                 val pulsePistolLevel: Double = 0,
                 val flakCanonLevel: Double = 0,
                 val spreadRifleLevel: Double = 0,
                 val rocketLauncherLevel: Double = 0,
                 val particleBallLevel: Double = 0,
                 var creationTime: Long = System.currentTimeMillis())