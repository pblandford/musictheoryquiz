package com.philblandford.musictheory.ui

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.scale
import com.philblandford.kscore.engine.core.representation.Standalone

@Composable
fun ScoreView(standalone: Standalone, available: Int, modifier: Modifier = Modifier) {

  val scale = if (standalone.width > available) {
    available.toFloat() / standalone.width
  } else 1f
    Canvas(modifier) {
      scale(scale) {
        standalone.draw(this)
      }
  }
}
