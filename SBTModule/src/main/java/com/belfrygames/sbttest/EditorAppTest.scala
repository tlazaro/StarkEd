package com.belfrygames.sbttest

import com.belfrygames.starkengine.core.Resources
import com.belfrygames.starkengine.core.Config
import com.belfrygames.starkengine.core.StarkApp

object EditorAppTest {
  def getApp() = {
    val resources = new Resources() {
      def initialize() {
        set("eddard", load("com/belfrygames/sbttest/eddard.png"))
        set("cursor", load("com/belfrygames/sbttest/cursor.png"))
        set("death", load("com/belfrygames/sbttest/death.png"))
      }
    }
    
    new EditorAppTest(new Config(resources, new ScreenTest()))
  }
}

class EditorAppTest(config: Config) extends StarkApp(config) {
  
}
