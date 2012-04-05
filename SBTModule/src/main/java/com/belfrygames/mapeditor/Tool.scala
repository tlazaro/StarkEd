package com.belfrygames.mapeditor

sealed trait Tool
case class Brush(tile: Tile) extends Tool
