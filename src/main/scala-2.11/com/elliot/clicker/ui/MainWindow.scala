package com.elliot.clicker.ui

import java.io.IOException
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafxml.core.{NoDependencyResolver, FXMLView}

object MainWindow extends JFXApp {

  System.loadLibrary("ClickBotWin32DLL")
  val resource = getClass.getResource("/MainWindow.fxml")

  val root = FXMLView(resource, NoDependencyResolver)

  stage = new PrimaryStage() {
    title = "Time Clickers Bot"
    scene = new Scene(root)
  }
}