package com.belfrygames.mapeditor

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.belfrygames.starkengine.core.Drawable
import com.belfrygames.starkengine.core.Updateable

class Layer(private var width: Int, private var height: Int, val tileWidth: Int, val tileHeight: Int) extends Drawable with Updateable {
  private var tiles = Array.ofDim[Tile](height, width)
  
  def apply(x: Int, y: Int) = {
    tiles(y)(x)
  }
  
  def update(x: Int, y: Int, tile: Tile) {
    tiles(y)(x) = tile
  }
  
  def resize(width: Int, height: Int) {
    
  }
  
  def fill(tileset: TileSet) {
    for(y <- 0 until height; x <- 0 until width) {
      this(x, y) = tileset(0)
    }
  }
  
  override def debugDraw(spriteBatch: ShapeRenderer) {
  }
  
  override def draw(spriteBatch: SpriteBatch) {
    for(y <- 0 until tiles.length; x <- 0 until tiles(0).length; tile = this(x, y); if tile != null) {
      tile.x = x * tileWidth
      tile.y = (tiles.length - 1 - y) * tileHeight
      tile.draw(spriteBatch)
    }
  }
}
