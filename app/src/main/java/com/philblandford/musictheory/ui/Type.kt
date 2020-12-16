package com.philblandford.musictheory.ui

import androidx.compose.material.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val typographyPhone = Typography(
  defaultFontFamily = FontFamily.SansSerif,
  body1 = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 16.sp
  ),
  h1 = TextStyle(
    fontWeight = FontWeight.Bold,
    fontSize = 25.sp
  )
)

val typographyTablet = Typography(
  defaultFontFamily = FontFamily.SansSerif,
  body1 = TextStyle(
    fontWeight = FontWeight.Normal,
    fontSize = 25.sp
  ),
  h1 = TextStyle(
    fontWeight = FontWeight.Bold,
    fontSize = 30.sp
  )
)