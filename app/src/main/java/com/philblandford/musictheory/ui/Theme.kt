package com.philblandford.musictheory.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.philblandford.musictheory.LocalDimensions

private val DarkColorPalette = darkColors(
  primary = purple200,
  primaryVariant = purple700,
  secondary = teal200,
)

private val LightColorPalette = lightColors(
  primary = lightBlue,
  primaryVariant = cyan,
  secondary = cyan,
  onPrimary = Color.White,
  onSurface = indigo,
  onSecondary = indigo,
  background = Color.White,
  onBackground = Color.Black



/* Other default colors to override
  background = Color.White,
  surface = Color.White,
  onPrimary = Color.White,
  onSecondary = Color.Black,
  onBackground = Color.Black,
  onSurface = Color.Black,
  */
)

@Composable
fun BoxWithConstraintsScope.MusicTheoryTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
  val colors = if (darkTheme) {
    DarkColorPalette
  } else {
    LightColorPalette
  }
  val block = block()

  CompositionLocalProvider(LocalDimensions provides Dimensions(block)) {

    MaterialTheme(
      colors = colors,
      typography = if (isPhone()) typographyPhone else typographyTablet,
      shapes = shapes,
      content = content
    )
  }
}

@Composable
fun ThemeBox(content: @Composable () -> Unit) {
  BoxWithConstraints(Modifier.fillMaxSize()) {
    MusicTheoryTheme() {
      content()
    }
  }
}