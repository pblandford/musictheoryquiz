package com.philblandford.musictheory.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.WithConstraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel

@Composable
fun ChooseLevel(quizName: String, modifier: Modifier = Modifier, cmd: (Int) -> Unit) {

  val viewModel = viewModel<ChooseLevelViewModel>()

  val levels = viewModel.getLevels()

  if (levels.isEmpty()) {
    cmd(0)
  } else {

    WithConstraints(modifier.fillMaxWidth()) {

      Column(
        modifier.fillMaxWidth().padding(horizontal = 20.dp)
          .background(MaterialTheme.colors.background).border(2.dp, MaterialTheme.colors.onSecondary)
      ) {

        Text(
          quizName, Modifier.align(Alignment.CenterHorizontally),
          color = MaterialTheme.colors.onSecondary,
          style = MaterialTheme.typography.h4
        )

        levels.forEach { level ->

          Spacer(Modifier.size(block() / 2))
          Box(
            Modifier.fillMaxWidth().padding(10.dp).border(2.dp, MaterialTheme.colors.onSecondary)
              .clickable(onClick = { cmd(level.num) })) {
            Text(
              level.description, Modifier.align(Alignment.Center).padding(5.dp, 10.dp),
              color = MaterialTheme.colors.onSecondary,
              style = MaterialTheme.typography.body1
            )
          }
        }
      }
    }
  }
}