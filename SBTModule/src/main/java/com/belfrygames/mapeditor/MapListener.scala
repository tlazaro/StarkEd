package com.belfrygames.mapeditor

trait MapListener {
  def mapChanged(map: StarkMap): Unit
}
