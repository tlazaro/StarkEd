package com.belfrygames.mapeditor

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.belfrygames.starkengine.core.Drawable
import com.belfrygames.starkengine.core.Updateable

class Layer(val name: String, private var width: Int, private var height: Int, val tileWidth: Int, val tileHeight: Int, var tileSet: TileSet) extends Drawable with Updateable {
  private var tiles = Array.ofDim[Int](height, width)
  
  def apply(x: Int, y: Int): Tile = {
    if (tileSet == null)  {
      null
    } else {
      tileSet(tiles(y)(x))
    }
  }
  
  def valueAt(x: Int, y: Int): Int = {
    tiles(y)(x)
  }
  
  def update(x: Int, y: Int, tile: Int) {
    tiles(y)(x) = tile
  }
  
  def resize(width: Int, height: Int) {
    
  }
  
  def fill(tileSet: TileSet) {
    this.tileSet = tileSet
    for(y <- 0 until height; x <- 0 until width) {
      this(x, y) = 0
    }
  }
  
  def getWidth = width
  def getHeight = height
  
  override def debugDraw(spriteBatch: ShapeRenderer) {
  }
  
  override def draw(spriteBatch: SpriteBatch) {
    if (tileSet != null) {
      for(y <- 0 until tiles.length; x <- 0 until tiles(0).length; tile = this(x, y); if tile != null) {
        tile.x = x * tileWidth
        tile.y = (tiles.length - 1 - y) * tileHeight
        tile.draw(spriteBatch)
      }
    }
  }
}
