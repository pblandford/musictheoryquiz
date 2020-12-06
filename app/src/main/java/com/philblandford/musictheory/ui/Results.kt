package com.philblandford.musictheory.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.WithConstraints
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.philblandford.musictheory.R
import com.philblandford.musictheory.engine.Quiz
import com.philblandford.musictheory.ui.Animation

@Composable
fun Results(onComplete: () -> Unit) {

  val viewModel = viewModel<ResultsViewModel>()

  viewModel.getQuiz()?.let { quiz ->

    WithConstraints(Modifier.fillMaxSize()) {
      val block = block()
      ConstraintLayout(Modifier.fillMaxSize()) {

        val (text, ok, balloon) = createRefs()
        Text(
          stringResource(R.string.you_scored, quiz.currentScore, quiz.numQuestions),
          Modifier.constrainAs(text) {
            centerTo(parent)
          }, style = MaterialTheme.typography.h5
        )
        Button(onComplete, Modifier.constrainAs(ok) {
          top.linkTo(text.bottom, block)
          centerHorizontallyTo(parent)
        }) {
          Text("OK")
        }
        Box(Modifier.constrainAs(balloon) {
          top.linkTo(parent.top)
          bottom.linkTo(parent.bottom)
          start.linkTo(parent.start)
          end.linkTo(parent.end)
          width = Dimension.fillToConstraints
          height = Dimension.fillToConstraints
        }.background(Color.Transparent)) {
          val height = constraints.maxHeight.toFloat() / AmbientDensity.current.density
          Animation(height, 5000) { offset ->
            Image(
              imageResource(R.drawable.balloon),
              Modifier.offset(y = (height.dp - offset.dp)).size(block()*6))
          }
        }
      }
    }
  }
}