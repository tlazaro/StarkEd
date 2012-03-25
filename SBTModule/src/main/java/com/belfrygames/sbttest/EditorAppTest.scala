package com.belfrygames.sbttest

import com.belfrygames.starkengine.core.Resources
import com.belfrygames.starkengine.core.Config
import com.belfrygames.starkengine.core.StarkApp
import com.belfrygames.mapeditor.MapScreen

object EditorAppTest {
  def getApp() = {
    val resources = new Resources() {
      def initialize() {
        set("eddard", Resources.load("com/belfrygames/sbttest/eddard.png"))
        set("cursor", Resources.load("com/belfrygames/sbttest/cursor.png"))
        set("death", Resources.load("com/belfrygames/sbttest/death.png"))
      }
    }
    
    new EditorAppTest(new Config(resources, new MapScreen()))
  }
}

class EditorAppTest(config: Config) extends StarkApp(config) {
  def getMapScreen(): MapScreen = {
    config.firstScreen.asInstanceOf[MapScreen]
  }
}
