package com.belfrygames.mapeditor

import com.badlogic.gdx.graphics.g2d.TextureRegion

object TileSet {
  def fromSplitTexture(regions: Array[Array[TextureRegion]]): TileSet = {
    val regs = regions.flatten.toIndexedSeq
    new TileSet(for(i <- 0 until regs.size) yield Tile(regs(i), i))
  }
}

class TileSet(val tiles: IndexedSeq[Tile]) {
  def apply(id: Int) = {
    if (id < 0) null else tiles(id)
  }
}
