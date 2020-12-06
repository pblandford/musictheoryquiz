package com.philblandford.musictheory.ui

import android.util.Log
import androidx.compose.animation.animatedFloat
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollableColumn
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.onActive
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.WithConstraints
import androidx.compose.ui.layout.WithConstraintsScope
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.philblandford.kscore.engine.duration.Chord
import com.philblandford.kscore.engine.types.Accidental
import com.philblandford.kscore.engine.types.Pitch

@Composable
fun<T> AnimatedGrid(
  numColumns:Int,
  items: List<T>,
  modifier: Modifier,
  child:@Composable BoxScope.(T, Modifier)->Unit,
) {
  WithConstraints(modifier.fillMaxSize()) {
    ScrollableColumn() {
      (0 until (items.size / numColumns)).forEach { row ->
        Row {
          val itemWidth = (constraints.maxWidth / AmbientDensity.current.density) / numColumns
          val itemHeight = (constraints.maxHeight / AmbientDensity.current.density) / (items.size/numColumns)
          val subSet = items.subList(row * numColumns, (row + 1) * numColumns)
          subSet.forEach { item ->
              Box(Modifier.size(itemWidth.dp, itemHeight.dp).border(2.dp, Color.White)) {
                Animation(itemWidth) { size ->
                  val ratio = size / itemWidth
                  val height = (itemHeight * ratio).dp
                  child(item, Modifier.size(size.dp, height).align(Alignment.Center))
                }
              }
            }
          }
        }
      }
    }
  }



@Composable
 fun Animation(target: Float, duration:Int = 1000, delay:Int = 0,children: @Composable (Float) -> Unit) {
  val percent = animatedFloat(0f)
  onActive {
    percent.animateTo(target, tween(duration, delay))
  }
  children(percent.value)
}

@Composable
fun WithConstraintsScope.block(): Dp {
  val div = if (isPhone()) 13  else 20
  val res = (maxWidth) / div
  Log.e("PHN", "$res")
  return res
}

@Composable
fun WithConstraintsScope.isPhone():Boolean {
  return maxWidth.value < 500
}

fun Chord.name():String {
  return notes.first().pitch.longName()
}

fun Pitch.longName():String {
  val accString = when (accidental) {
    Accidental.FLAT -> "Flat"
    Accidental.SHARP -> "Sharp"
    else -> ""
  }
  return "$noteLetter $accString"
}

fun String.qualityFull():String {
  return when (this) {
    "m" -> "Minor"
    else -> "Major"
  }
}