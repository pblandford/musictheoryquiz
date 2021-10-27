package com.philblandford.musictheory.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.philblandford.musictheory.LocalDimensions

@Composable
fun ChooseLevel(quizName: String, modifier: Modifier = Modifier,
                setLevel: (Int?) -> Unit) {

  val viewModel = viewModel<ChooseLevelViewModel>()

  val levels = viewModel.getLevels()
  val spaceHeight = LocalDimensions.current.block / 2

  if (levels.isEmpty()) {
    setLevel(null)
  } else {


    Column(
      modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp)
        .background(MaterialTheme.colors.background)
        .border(2.dp, MaterialTheme.colors.onBackground)
    ) {

      Text(
        quizName, Modifier.align(Alignment.CenterHorizontally),
        color = MaterialTheme.colors.onBackground,
        style = MaterialTheme.typography.h4
      )

      levels.forEach { level ->

        Spacer(Modifier.size(spaceHeight))
        Box(
          Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .border(2.dp, MaterialTheme.colors.onBackground)
            .clickable(onClick = { setLevel(level.num) })
        ) {
          Text(
            level.description,
            Modifier
              .align(Alignment.Center)
              .padding(5.dp, 10.dp),
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.body1
          )
        }
      }
    }
  }

}