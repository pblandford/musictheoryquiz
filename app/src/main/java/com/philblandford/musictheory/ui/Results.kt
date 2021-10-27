package com.philblandford.musictheory.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.philblandford.musictheory.R
import com.philblandford.musictheory.engine.Quiz

@Composable
fun Results(onComplete: () -> Unit) {

  val viewModel = viewModel<ResultsViewModel>()

  viewModel.getQuiz()?.let { quiz ->
    ResultsInternal(quiz.currentScore, quiz.numQuestions) {
      viewModel.receiveIntent(UIIntent.QuizComplete)
      onComplete()
    }
  }

}

@Composable
fun ResultsInternal(currentScore: Int, numQuestions: Int, onComplete: () -> Unit) {

  Box(Modifier.fillMaxSize()) {

    Column(
      Modifier.align(Alignment.Center),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        stringResource(R.string.you_scored, currentScore, numQuestions),
        style = MaterialTheme.typography.h5
      )
      Spacer(Modifier.height(10.dp))
      Button(onComplete) {
        Text("OK")
      }
    }
    Balloon()
  }
}

@Composable
 fun Balloon() {
  BoxWithConstraints(
    Modifier
      .fillMaxSize()
      .background(Color.Transparent)
  ) {
    val height = maxHeight
    val imageHeight = block()*6
    val finished = remember { mutableStateOf(false) }
    val offset = animateDpAsState(if (!finished.value) imageHeight else maxHeight,
    tween(1500))

    LaunchedEffect(Unit) {
      finished.value = true
    }

    Image(
      painterResource(R.drawable.balloon), "Balloon",
      Modifier
        .offset(y = (height - offset.value))
        .size(imageHeight)
    )

  }
}

@Composable
@Preview
private fun Preview() {
  ResultsInternal(5, 10) {

  }
}