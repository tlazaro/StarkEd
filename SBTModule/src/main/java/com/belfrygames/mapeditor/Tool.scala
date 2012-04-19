package com.belfrygames.mapeditor

sealed trait Tool
case object Selection extends Tool
case object Eraser extends Tool
case object Brush extends Tool
case object BucketFill extends Tool
