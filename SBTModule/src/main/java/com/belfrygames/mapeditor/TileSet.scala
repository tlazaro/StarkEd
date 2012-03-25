package com.belfrygames.mapeditor

import com.badlogic.gdx.graphics.g2d.TextureRegion

object TileSet {
  def fromSplitTexture(regions: Array[Array[TextureRegion]]): TileSet = {
    val regs = regions.flatten.toIndexedSeq
    new TileSet(for(i <- 0 until regs.size) yield Tile(regs(i)))
  }
}

class TileSet(val tiles: IndexedSeq[Tile]) {
  def apply(id: Int) = {
    tiles(id)
  }
}
