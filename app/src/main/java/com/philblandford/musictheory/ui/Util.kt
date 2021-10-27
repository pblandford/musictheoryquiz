package com.philblandford.musictheory.ui

import android.util.Log
import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.philblandford.kscore.engine.duration.Chord
import com.philblandford.kscore.engine.types.Accidental
import com.philblandford.kscore.engine.types.Pitch


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <T> AnimatedGrid(
  numColumns: Int,
  items: List<T>,
  modifier: Modifier,
  child: @Composable BoxScope.(T, Modifier) -> Unit,
) {
  BoxWithConstraints(modifier.fillMaxSize()) {
    val width = maxWidth
    val height = maxHeight
    val scope = this

    Column(Modifier.fillMaxSize()) {
      (0 until (items.size / numColumns)).forEach { row ->
        Row {
          val itemWidth = width / numColumns
          val itemHeight = height / (items.size / numColumns)
          val subSet = items.subList(row * numColumns, (row + 1) * numColumns)
          subSet.forEach { item ->
            Item<T>(itemWidth, itemHeight) {
              scope.child(item, Modifier.fillMaxSize())
            }
          }
        }
      }
    }
  }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun <T> Item(
  itemWidth: Dp, itemHeight: Dp,
  child: @Composable () -> Unit
) {
  val visible = remember { mutableStateOf(false) }

  LaunchedEffect(Unit) {
    visible.value = true
  }

  Box(
    Modifier
      .size(itemWidth, itemHeight)
      .border(2.dp, Color.White)
  ) {

    AnimatedVisibility(
      visible = visible.value,
      enter = scaleIn(animationSpec =
      tween(1000, easing = LinearOutSlowInEasing))
    ) {
      child()
    }
  }
}


@Composable
fun BoxWithConstraintsScope.block(): Dp {
  val div = if (isPhone()) 13 else 20
  val res = (maxWidth) / div
  Log.e("PHN", "$res")
  return res
}

@Composable
fun BoxWithConstraintsScope.isPhone(): Boolean {
  return maxWidth.value < 500
}

fun Chord.name(): String {
  return notes.first().pitch.longName()
}

fun Pitch.longName(): String {
  val accString = when (accidental) {
    Accidental.FLAT -> "Flat"
    Accidental.SHARP -> "Sharp"
    else -> ""
  }
  return "$noteLetter $accString".trim()
}

fun String.qualityFull(): String {
  return when (this) {
    "m" -> "Minor"
    else -> "Major"
  }
}