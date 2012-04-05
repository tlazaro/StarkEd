package com.belfrygames.mapeditor

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType
import com.belfrygames.starkengine.core.DrawableParent
import com.belfrygames.starkengine.core.Resources
import com.belfrygames.starkengine.core.UpdateableParent
import com.belfrygames.starkengine.tags._
import scala.collection.mutable.ListBuffer

class StarkMap(private var width0: Int, private var height0: Int, var tileWidth: Int, var tileHeight: Int) extends DrawableParent with UpdateableParent {
  def width = width0
  def width_=(value: Int) {
    width0 = value
  }
    
  def height: Int = height0
  def height_=(value: Int) {
    height0 = value
  }
  
  var layers = new ListBuffer[Layer]()
  
  override def update(elapsed : Long @@ Milliseconds) {
    super.update(elapsed)
  }
  
  def applyUpdate(update: JSON.ParseResult[Any]) {
    val isMap: PartialFunction[Any, Map[String, Any]] = {case n: Map[String, Any] => n}
    val isNumber: PartialFunction[Any, Double] = {case n: Double => n}
    val isString: PartialFunction[Any, String] = {case n: String => n}
    
    update match {
      case p: JSON.Success[Any] => {
          p.get match {
            case obj : Map[String, Any] => {
                println("Working on a map...")
                for((key, value) <- obj) {
                  println("Found: " + key + ", " + value)
                }
                
                for(map <- obj.get("map").collect(isMap); a = println("map ok");
                    layersDef <- map.get("layers").collect(isMap); a = println("layers ok");
                    w <- map.get("width").collect(isNumber); a = println("width ok");
                    h <- map.get("height").collect(isNumber); a = println("height ok");
                    tWidth <- map.get("tileWidth").collect(isNumber); a = println("tileWidth ok");
                    tHeight <- map.get("tileHeight").collect(isNumber); a = println("tileHeight ok");
                    tileSetName <- map.get("tileSet").collect(isString); a = println("tileSet ok")) {
                
                  println("Looking good...")
                  
                  width = w.toInt
                  height = h.toInt
                  tileWidth = tWidth.toInt
                  tileHeight = tHeight.toInt
                    
                  val tileSet = TileSet.fromSplitTexture(Resources.split("com/belfrygames/mapeditor/terrenos.png", tileWidth, tileHeight, 1, 2, false, false))
                
                  clearLayers()
                  for((key, value) <- layersDef; list = value.asInstanceOf[List[List[Double]]]) {
                    val layer = addLayer(key)
                    println("Adding layer named: " + key + " wiht values:" + value)
                    for(y <- 0 until list.size; x <- 0 until list.head.size) {
                      layer(x, y) = tileSet(list(y)(x).toInt)
                    }
                  }
                }
              }
            case _ => println("INVALID JSON UPDATE CANT FIND 'map'")
          }
        }
      case _ => {
          println("INVALID JSON UPDATE")
        }
    }
  }
  
  def clearLayers() = {
    for (layer <- layers) {
      removeDrawable(layer)
      removeUpdateable(layer)
    }
    layers.clear()
  }
  
  def addLayer(name: String, at: Int = -1): Layer = {
    val layer = new Layer(name, width, height, tileWidth, tileHeight)
    if (at < 0 || at >= layers.size) {
      layers.append(layer)
    } else {
      layers.insert(at, layer)
    }
    
    addDrawable(layer)
    addUpdateable(layer)
    layer
  }
  
  def removeLayer(at: Int) {
    val layer = layers.remove(at)
    removeDrawable(layer)
    removeUpdateable(layer)
  }
  
  def removeLayer(layer: Layer) {
    removeLayer(layers indexOf layer)
  }
  
  def layerNames(): Array[String] = layers.map(_.name).toArray
  
  def fromTexture(regions: Array[Array[TextureRegion]]) {
    val tiles = TileSet.fromSplitTexture(regions)
    val layer = addLayer("background", 0)
    layer.fill(tiles)
  }
  
  override def debugDraw(renderer: ShapeRenderer) {
    super.debugDraw(renderer)
    
    renderer.setColor(1f, 1f, 1f, 1f)
    renderer.begin(ShapeType.Line)
    renderer.identity()
    for(x <- Range(0, width * tileWidth, tileWidth).inclusive) {
      renderer.line(x, 0, x, height * tileHeight)
    }
    for(y <- Range(0, height * tileHeight, tileHeight).inclusive) {
      renderer.line(0, y, width * tileWidth, y)
    }
    renderer.end()
  }
  
  private def resizeLayers() {
    for(layer <- layers) {
      layer.resize(width, height)
    }
  }
}
