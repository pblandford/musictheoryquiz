package com.philblandford.musictheory.ui

import com.philblandford.kscore.engine.types.ClefType

sealed class UIIntent {
  data class SelectAnswer(val idx:Int) : UIIntent()
  object NextQuestion : UIIntent()
  data class SelectClef(val clefType: ClefType, val yes:Boolean) : UIIntent()
}

