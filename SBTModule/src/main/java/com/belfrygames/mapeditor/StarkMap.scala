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
  var tileSet: TileSet = null
  private var listener: Option[MapListener] = None
  
  def clearListener() = listener = None
  def setListener(listener: MapListener) {
    this.listener = Some(listener)
    fireMapChanged()
  }
  
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
                for(map <- obj.get("map").collect(isMap);
                    layersDef <- map.get("layers").collect(isMap);
                    w <- map.get("width").collect(isNumber);
                    h <- map.get("height").collect(isNumber);
                    tWidth <- map.get("tileWidth").collect(isNumber);
                    tHeight <- map.get("tileHeight").collect(isNumber);
                    tileSetName <- map.get("tileSet").collect(isString)) {
                
                  width = w.toInt
                  height = h.toInt
                  tileWidth = tWidth.toInt
                  tileHeight = tHeight.toInt
                    
                  tileSet = TileSet.fromSplitTexture(Resources.split("com/belfrygames/mapeditor/terrenos.png", tileWidth, tileHeight, 1, 2, false, false))
                
                  clearLayers()
                  for((key, value) <- layersDef; list = value.asInstanceOf[List[List[Double]]]) {
                    val layer = addLayer(key)
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
  
  def serializeText(): String = {
    var ind = 0
    def indent = " " * (ind * 2)
    def indentValue(value: Int) = " " * (value * 2)
    
    val b = new StringBuilder
    b append "{\n"; ind += 1
    b append indent + "\"map\": {\n"; ind += 1
    
    b append indent + "\"width\": " + width + ",\n"
    b append indent + "\"height\": " + height + ",\n"
    b append indent + "\"tileWidth\": " + tileWidth + ",\n"
    b append indent + "\"tileHeight\": " + tileHeight + ",\n"
    b append indent + "\"tileSet\": " + "\"com/belfrygames/mapeditor/terrenos.png\"" + ",\n"
    
    b append indent + "\"layers\": {\n"; ind += 1
    for(layer <- layers) {
      b append indent + "\"" + layer.name + "\": "; ind += 1
      
      b append (for(y <- 0 until layer.getHeight) yield {
          (for(x <- 0 until layer.getWidth) yield {
              val tile = layer(x, y)
              if (tile != null) tile.id else -1
            }).mkString(indent + "[", ", ", "]")
        }).mkString("[\n", ",\n", "\n" + indentValue(ind - 1) + "],\n")
      ind -= 1
    }
    
    if (!layers.isEmpty)
      b.deleteCharAt(b.length - 2)
    
    while(ind > 0) {
      ind -= 1
      b append indent + "}\n"
    }
    
    b.toString
  }
  
  def clearLayers() = {
    for (layer <- layers) {
      removeDrawable(layer)
      removeUpdateable(layer)
    }
    layers.clear()
  }
  
  def addLayer(layer: Layer, at: Int): Layer = {
    if (at < 0 || at >= layers.size) {
      layers.append(layer)
    } else {
      layers.insert(at, layer)
    }
    
    addDrawable(layer)
    addUpdateable(layer)
    layer
  }
  
  def addLayer(name: String, at: Int = -1): Layer = {
    val layer = new Layer(name, width, height, tileWidth, tileHeight)
    addLayer(layer, at)
  }
  
  def organizeLayers(names: Array[String], visible: Array[Boolean]): Boolean = {
    if (names.length != visible.length || names.length != layers.size) {
      println("Wrong names or visible sizes")
      return false
    }
    
    val mapped = names.map(name => layers.find(_.name == name)).flatten
    if (mapped.length != layers.size) {
      println("Layers not found")
      return false
    }
    
    clearLayers()
    mapped.foreach(addLayer(_, -1))
    
    for(i <- 0 until visible.length) {
      setVisible(i, visible(i))
    }
    
    fireMapChanged()
    true
  }
  
  def setVisible(index: Int, visible: Boolean) {
    layers(index).visible = visible
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
  def layerVisible(): Array[Boolean] = layers.map(_.visible).toArray
  
  def fromTexture(regions: Array[Array[TextureRegion]]) {
    val tiles = TileSet.fromSplitTexture(regions)
    val layer = addLayer("background", 0)
    layer.fill(tiles)
  }
  
  var currentLayer = -1
  def getCurrentLayer: Layer = if (currentLayer < 0) layers.last else layers(currentLayer)
  def setCurrentLayer(index: Int) {
    if (index < 0 || index >= layers.size) {
      // ignore
      println("Invalid Layer index: " + index)
    }
    currentLayer = index
  }
  
  def applyTool(x: Float, y: Float, tool: Tool): Boolean = {
    var change = false
    if ((0 <= x && x < tileWidth * width) && (0 <= y && y < tileHeight * height)) {
      val xCoord = (x / tileWidth).toInt
      val yCoord = height - 1 - (y / tileHeight).toInt
    
      tool match {
        case Brush(tile) => {
            val old = getCurrentLayer(xCoord, yCoord)
            if (tile != old) {
              getCurrentLayer(xCoord, yCoord) = tile
              change = true
              fireMapChanged()
            }
          }
      }
    }
    change
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
  
  private def fireMapChanged() {
    listener foreach (_.mapChanged(this))
  }
}
