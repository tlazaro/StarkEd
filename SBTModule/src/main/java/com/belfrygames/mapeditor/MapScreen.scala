package com.belfrygames.mapeditor

import com.belfrygames.starkengine.core._
import com.belfrygames.starkengine.tags._

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector3

class MapScreen extends Screen {
  def getStarkMap() = starkMap
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
      starkMap.applyUpdate(postUpdate.get)
      postUpdate = None
    }
    
    if (postListener.isDefined) {
      starkMap.setListener(postListener.get)
      postListener = None
    } 
    
    super.update(elapsed)
  }
  
  var postUpdate: Option[JSON.ParseResult[Any]] = None
  def postUpdate(parsed: JSON.ParseResult[Any]) {
    postUpdate = Some(parsed)
  }
  
  var postListener: Option[MapListener] = None
  def postMapListener(listener: MapListener) {
    postListener = Some(listener)
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
  
  def screenToCanvas(x: Int, y: Int, result: Vector3 = null): Vector3 = {
    val vec = if (result == null) new Vector3 else result
    vec.x = screenToViewPortX(x)
    vec.y = screenToViewPortY(y)
    vec.z = 0
    cam.unproject(vec)
    vec
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
      screenToCanvas(x, y, tmp)
      
      cursor.x = tmp.x
      cursor.y = tmp.y
      
      false
    }
    
    override def touchDown(x: Int, y: Int, pointer: Int, button: Int) = {
      println("Touch down at: [" + x + ", " + y + "] button: " + button)
      
      button match {
        case Input.Buttons.LEFT => {
            screenToCanvas(x, y, tmp)
            starkMap.applyTool(tmp.x, tmp.y, Brush(starkMap.tileSet(0)))
        }
        case _ => false
      }
    }
  }
}
