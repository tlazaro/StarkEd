package com.belfrygames.mapeditor

import com.belfrygames.starkengine.core._
import com.belfrygames.starkengine.tags._

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.math.Vector3

class MapScreen(val starkMap: StarkMap) extends Screen {
  def getStarkMap() = starkMap
  lazy val cursor = Sprite(app.res.get("cursor"))
  
  override def register() {
    super.register()
  }
  
  override def deregister() {
    super.deregister()
  }
  
  def getMapSize() = {
    if (starkMap == null) {
      new java.awt.Dimension(200, 200)
    } else {
      new java.awt.Dimension(starkMap.width.toInt, starkMap.height.toInt)
    }
  }
  
  override def create(app: StarkApp) {
    super.create(app)
    
    Screen.DEBUG = true
    Screen.SHOW_KEYS = false
    
    app.resizePolicy = Original
    app.resize(Gdx.graphics.getWidth, Gdx.graphics.getHeight)
    
    foreground.add(cursor, "cursor")
    cursor.setOrigin(0.5f, 0.5f)
    
    foreground.add(starkMap, "starkMap")
    
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
  def postUpdate(text: String) {
    postUpdate = Some(JSON.parseText(text))
  }
  
  var postListener: Option[MapListener] = None
  def postMapListener(listener: MapListener) {
    postListener = Some(listener)
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
    
    override def touchDragged(x: Int, y: Int, pointer: Int): Boolean = {
      screenToCanvas(x, y, tmp)
      
      cursor.x = tmp.x
      cursor.y = tmp.y
      
      if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
        starkMap.applyTool(tmp.x, tmp.y, starkMap.getCurrentTool)
      } else {
        false
      }
    }
    
    override def touchDown(x: Int, y: Int, pointer: Int, button: Int) = {
      button match {
        case Input.Buttons.LEFT => {
            screenToCanvas(x, y, tmp)
            if (starkMap != null) {
              starkMap.applyTool(tmp.x, tmp.y, starkMap.getCurrentTool)
            }  else {
              false
            }
          }
        case _ => false
      }
    }
  }
}
