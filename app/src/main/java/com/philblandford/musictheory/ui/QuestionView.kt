package com.philblandford.musictheory.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.philblandford.kscore.engine.core.representation.StandaloneGenerator
import com.philblandford.kscore.engine.duration.Chord
import com.philblandford.kscore.engine.duration.Note
import com.philblandford.kscore.engine.duration.crotchet
import com.philblandford.kscore.engine.types.NoteLetter
import com.philblandford.kscore.engine.types.Pitch
import com.philblandford.kscoreandroid.drawingcompose.ComposeDrawableGetter
import com.philblandford.kscoreandroid.text.AndroidTextFontManager
import com.philblandford.musictheory.engine.Question
import com.philblandford.musictheory.R
import com.philblandford.musictheory.StubTextFontManager
import com.philblandford.musictheory.engine.NoteQuiz

@Composable
fun QuestionView(onComplete: () -> Unit) {
  Log.e("QV", "WTF")
  val questionViewModel = viewModel<QuestionViewModel>()

  questionViewModel.startQuiz()

  val modelState = questionViewModel.getModel().observeAsState()

  modelState.value?.let { questionModel ->
    if (questionModel.complete) {
      questionViewModel.receiveIntent(UIIntent.ClearCompleteFlag)
      onComplete()
    } else {
      QuestionViewInternal(questionModel) { questionViewModel.receiveIntent(it) }
    }
  }
}

@Composable
private fun QuestionViewInternal(
  questionModel: QuestionModel,
  cmd: (UIIntent) -> Unit
) {
  questionModel.question?.let { question ->
    BoxWithConstraints(Modifier.fillMaxSize()) {
      val width = maxWidth
      val block = block()

      Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
        QuestionText(question.text, Modifier.padding(10.dp))
        Spacer(Modifier.height(70.dp))
        ScoreView(
          question.standalone, width.value.toInt(),
          Modifier.fillMaxHeight(0.3f).align(Alignment.Start).offset(x = block*2)
        )
        Answers(
          question, questionModel.highlightAnswer,
          Modifier
            .wrapContentHeight()
        ) {
          cmd(it)
        }
        Box(Modifier.weight(1f))
        ScoreRow(questionModel, Modifier.fillMaxWidth())
      }
    }
  }
}

@Composable
private fun QuestionText(question: String, modifier: Modifier) {
  Text(question, modifier, style = MaterialTheme.typography.h5)
}

@Composable
private fun Answers(
  question: Question, highlightAnswer: Boolean,
  modifier: Modifier, cmd: (UIIntent) -> Unit
) {
  Column(modifier) {
    question.answers.withIndex().toList().forEach {
      Answer(
        it.value,
        Modifier.height(80.dp),
        highlightAnswer && it.index == question.correctIdx
      ) {
        if (highlightAnswer) {
          cmd(UIIntent.NextQuestion)
        } else {
          cmd(UIIntent.SelectAnswer(it.index))
        }
      }
    }
  }
}

@Composable
private fun Answer(answer: String, modifier: Modifier, highlight: Boolean, cmd: () -> Unit) {
  Box(
    modifier
      .fillMaxWidth()
      .padding(10.dp)
      .clickable(onClick = cmd)
      .border(1.dp, MaterialTheme.colors.secondary)
      .background(if (highlight) red else cyan)
  ) {
    Text(
      answer,
      Modifier
        .align(Alignment.Center)
        .padding(10.dp),
      color = MaterialTheme.colors.onSecondary,
      style = MaterialTheme.typography.h5
    )
  }
}

@Composable
private fun ScoreRow(model: QuestionModel, modifier: Modifier) {
  BoxWithConstraints(
    modifier
      .fillMaxWidth()
      .padding(horizontal = 5.dp)
  ) {
    val itemWidth = maxWidth / model.numQuestions
    Row(
      Modifier
        .fillMaxWidth()
        .height(itemWidth)
        .padding(5.dp)
    ) {
      model.answers.forEach { correct ->
        val color = if (correct) Color.Green else Color.Red
        Image(
          painterResource(R.drawable.star), "Star",
          Modifier.size(itemWidth),
          colorFilter = ColorFilter.tint(color)
        )
      }
    }
  }
}

@Composable
@Preview
private fun Preview() {
  val standaloneGenerator = StandaloneGenerator(
    ComposeDrawableGetter(LocalContext.current, StubTextFontManager()))

  val question  = Question("Wut dat?", listOf("A fish", "A giraffe", "A fridge", "A yeti"),
  2, standaloneGenerator.getChord(Chord(crotchet(), listOf(Note(crotchet(), Pitch(NoteLetter.A)
    ))))!!)
  ThemeBox {
    QuestionViewInternal(
      QuestionModel(question, listOf(true, false, false, false), 10),{})
  }
}