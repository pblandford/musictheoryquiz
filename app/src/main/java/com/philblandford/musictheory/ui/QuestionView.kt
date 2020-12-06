package com.philblandford.musictheory.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumnFor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.WithConstraints
import androidx.compose.ui.layout.WithConstraintsScope
import androidx.compose.ui.platform.AmbientDensity
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import com.philblandford.musictheory.engine.Question
import com.philblandford.musictheory.R

@Composable
fun QuestionView(onComplete: () -> Unit) {
  val questionViewModel = viewModel<QuestionViewModel>()
  val modelState = questionViewModel.getModel().observeAsState()

  modelState.value?.let { questionModel ->

    if (questionModel.complete) {
      onComplete()
    }

    questionModel.question?.let { question ->
      WithConstraints(Modifier.fillMaxSize()) {
        val block = block()

        ConstraintLayout(Modifier.fillMaxSize()) {
          val (score, qText, display, answers) = createRefs()
          QuestionText(question.text, Modifier.constrainAs(qText) {
            top.linkTo(parent.top, 10.dp)
            start.linkTo(parent.start, block)
          })
          ScoreView(
            question.standalone,
            constraints.maxWidth - (block *2 * AmbientDensity.current.density).value.toInt(),
            Modifier.constrainAs(display) {
              top.linkTo(qText.bottom, block)
              bottom.linkTo(answers.top)
              start.linkTo(parent.start, block)
              height = Dimension.percent(0.2f)
              width = Dimension.fillToConstraints
            })
          Answers(question, questionModel.highlightAnswer, Modifier.constrainAs(answers) {
            bottom.linkTo(score.top)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            height = Dimension.wrapContent
          }) {
            questionViewModel.receiveIntent(it)
          }
          ScoreRow(questionModel, Modifier.constrainAs(score) {
            bottom.linkTo(parent.bottom)
          })
        }
      }
    }
  }
}

@Composable
private fun QuestionText(question: String, modifier: Modifier) {
  Text(question, modifier, style = MaterialTheme.typography.h5)
}

@Composable
private fun WithConstraintsScope.Answers(
  question: Question, highlightAnswer: Boolean,
  modifier: Modifier, cmd: (UIIntent) -> Unit
) {
  LazyColumnFor(question.answers.withIndex().toList(), modifier) {
    Answer(it.value, highlightAnswer && it.index == question.correctIdx) {
      if (highlightAnswer) {
        cmd(UIIntent.NextQuestion)
      } else {
        cmd(UIIntent.SelectAnswer(it.index))
      }
    }
  }
}

@Composable
private fun WithConstraintsScope.Answer(answer: String, highlight: Boolean, cmd: () -> Unit) {
  Box(
    Modifier.height(block() * 3).fillMaxWidth().padding(10.dp).clickable(onClick = cmd)
      .border(1.dp, MaterialTheme.colors.secondary).background(if (highlight) red else cyan)
  ) {
    Text(
      answer,
      Modifier.align(Alignment.Center).offset(10.dp),
      color = MaterialTheme.colors.onSecondary,
      style = MaterialTheme.typography.h5
    )
  }
}

@Composable
private fun ScoreRow(model: QuestionModel, modifier: Modifier) {
  WithConstraints(modifier.fillMaxWidth().padding(horizontal = 5.dp)) {
    val itemWidth = constraints.maxWidth / model.numQuestions / AmbientDensity.current.density
    Row(Modifier.fillMaxWidth().height(itemWidth.dp).padding(5.dp)) {
      model.answers.forEach { correct ->
        val color = if (correct) Color.Green else Color.Red
        Image(
          imageResource(R.drawable.star),
          Modifier.size(itemWidth.dp),
          colorFilter = ColorFilter.tint(color)
        )
      }
    }
  }
}