package com.belfrygames.mapeditor

sealed trait Tool
case class Brush(tileId: Int) extends Tool
