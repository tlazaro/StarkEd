package com.belfrygames.mapeditor

import com.belfrygames.starkengine.core._
import com.belfrygames.starkengine.tags._

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector3

class MapScreen extends Screen {
  
  var starkMap: StarkMap = null
  lazy val cursor = Sprite(app.res.get("cursor"))
  
  override def register() {
    super.register()
  }
  
  override def deregister() {
    super.deregister()
  }
  
  override def create(app: StarkApp) {
    super.create(app)
    
    Screen.DEBUG = true
    Screen.SHOW_KEYS = false
    
    app.resizePolicy = OriginalCanvas
    app.resize(Gdx.graphics.getWidth, Gdx.graphics.getHeight)
    
    addSprite(cursor)
    cursor.setOrigin(0.5f, 0.5f)
    
    setMap(new StarkMap(20, 20, 64, 64))
    starkMap.fromTexture(Resources.split("com/belfrygames/mapeditor/terrenos.png", starkMap.tileWidth, starkMap.tileHeight, 1, 2, false, false))
    
    app.inputs.addProcessor(new InputTest())
  }
  
  override def update(elapsed : Long @@ Milliseconds) {
    if (postUpdate.isDefined) {
      println("Applying Update...")
      starkMap.applyUpdate(postUpdate.get)
      postUpdate = None
    }
    
    super.update(elapsed)
  }
  
  var postUpdate: Option[JSON.ParseResult[Any]] = None
  def postUpdate(parsed: JSON.ParseResult[Any]) {
    postUpdate = Some(parsed)
  }
  
  def setMap(map: StarkMap) {
    if (starkMap != null) {
      regularCam.removeDrawable(starkMap)
      removeUpdateable(starkMap)
    }
    
    starkMap = map
    regularCam.addDrawable(starkMap)
    addUpdateable(starkMap)
  }
  
  class InputTest extends InputAdapter {
    import com.badlogic.gdx.Input.Keys._

    override def keyUp(keycode : Int) : Boolean = {
      keycode match {
        case F5 => Screen.DEBUG = !Screen.DEBUG
        case _ =>
      }

      false
    }
    
    private[this] val tmp = new Vector3
    override def touchMoved(x: Int, y: Int) = {
      tmp.x = screenToViewPortX(x)
      tmp.y = screenToViewPortY(y)
      tmp.z = 0
      cam.unproject(tmp)
      cursor.x = tmp.x
      cursor.y = tmp.y
      
      false
    }
  }
}
