package com.belfrygames.mapeditor

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.belfrygames.starkengine.core.Particle
import com.belfrygames.starkengine.core.Sprite

object Tile {
  def apply(region: TextureRegion) = {
    new Tile {
      textureRegion = region
    }
  }
}

trait Tile extends Particle with Sprite {
}
