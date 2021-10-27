package com.philblandford.musictheory.ui

import android.util.Log
import android.widget.PopupWindow
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.lifecycle.viewmodel.compose.viewModel
import com.philblandford.kscore.engine.types.ClefType
import com.philblandford.musictheory.LocalDimensions
import com.philblandford.musictheory.resources.QuizType
import com.philblandford.musictheory.R
import com.philblandford.musictheory.engine.Level
import com.philblandford.musictheory.resources.QuizDescriptor

@Composable
fun ChooseQuiz(onComplete: () -> Unit) {
  val viewModel = viewModel<ChooseQuizViewModel>()

  viewModel.getModel().observeAsState().value?.let { model ->
    ChooseQuizInternal(model, viewModel::receiveIntent, onComplete)
  }
}

@Composable
private fun ChooseQuizInternal(
  model: ChooseQuizModel,
  cmd: (UIIntent) -> Unit,
  onComplete: () -> Unit
) {

  val block = LocalDimensions.current.block

  Column(Modifier.fillMaxSize()) {

    Title(
      Modifier
        .height(block * 2)
        .fillMaxWidth()
        .background(Color.Blue)
    )

    Selection(model.quizzes,
      Modifier
        .fillMaxWidth()
        .weight(1f), cmd) { onComplete() }

    ClefSelection(model, cmd, Modifier.fillMaxWidth(), block)

  }
}


@Composable
private fun ClefSelection(
  model: ChooseQuizModel, cmd: (UIIntent) -> Unit,
  modifier: Modifier, imageHeight: Dp
) {

  Row(
    modifier,
    horizontalArrangement = Arrangement.SpaceEvenly,
    verticalAlignment = Alignment.CenterVertically
  ) {
    model.clefs.forEach { cd ->
      Checkbox(cd.enabled, onCheckedChange = { yes ->
        cmd(UIIntent.SelectClef(cd.clefType, yes))
      })
      Image(
        painterResource(cd.resId), stringResource(R.string.clef), Modifier.size(imageHeight),
        colorFilter = ColorFilter.tint(MaterialTheme.colors.onBackground)
      )
    }
  }
}

@Composable
private fun Title(modifier: Modifier) {
  Box(
    modifier
      .background(MaterialTheme.colors.primary)
      .border(2.dp, Color.White)
  ) {
    Text(
      stringResource(R.string.choose),
      Modifier.align(Alignment.Center),
      color = MaterialTheme.colors.onPrimary, style = MaterialTheme.typography.h1
    )
  }
}

@Composable
private fun Selection(
  items: List<QuizDescriptorDisplay>,
  modifier: Modifier,
  cmd: (UIIntent) -> Unit, onComplete: () -> Unit
) {

  val showPopup = remember { mutableStateOf<String?>(null) }

  showPopup.value?.let {
    LevelPopup(it, { showPopup.value = null }) { level ->
      level?.let {
        cmd(UIIntent.SetLevel(it))
      }
      onComplete()
    }
  }

  AnimatedGrid(2, items, modifier.fillMaxSize()) { qdd, mod ->
    Option(qdd, mod, { qt ->
      cmd(UIIntent.SelectQuiz(qt))
      showPopup.value = qdd.quizDescriptor.name
    }, onComplete)
  }
}

@Composable
private fun Option(
  quizDescriptor: QuizDescriptorDisplay, modifier: Modifier,
  select: (QuizType) -> Unit, onComplete: () -> Unit
) {


  BoxWithConstraints(
    modifier
      .background(quizDescriptor.color)
      .clickable(onClick = {
        select(quizDescriptor.quizDescriptor.type)
      })
  ) {
    Image(
      painterResource(id = quizDescriptor.resId), "Option",
      Modifier
        .fillMaxSize(0.5f)
        .background(Color.Transparent).align(Alignment.Center),
      colorFilter = ColorFilter.tint(MaterialTheme.colors.onSecondary)
    )
    Text(
      quizDescriptor.quizDescriptor.name,
      Modifier.align(Alignment.BottomCenter).offset(y = (-20).dp),
      color = MaterialTheme.colors.onSecondary
    )
  }

}

@Composable
private fun LevelPopup(quizName: String, dismiss: () -> Unit, onSelect: (Int?) -> Unit) {
  Popup(Alignment.Center, onDismissRequest = dismiss) {
    ChooseLevel(quizName, Modifier.fillMaxWidth(), onSelect)
  }
}

@Composable
@Preview
private fun Preview() {
  ThemeBox {
    ChooseQuizInternal(ChooseQuizModel(
      listOf(
        ClefDescriptor(ClefType.TREBLE, true, R.drawable.treble_clef),
        ClefDescriptor(ClefType.BASS, true, R.drawable.treble_clef),
        ClefDescriptor(ClefType.TREBLE, true, R.drawable.treble_clef),
        ClefDescriptor(ClefType.BASS, true, R.drawable.treble_clef)
      ),
      listOf(
        QuizDescriptorDisplay(
          QuizDescriptor(QuizType.INTERVAL, "Intervals"),
          R.drawable.interval,
          Color.Blue
        ),
        QuizDescriptorDisplay(
          QuizDescriptor(QuizType.KEY, "Keys"),
          R.drawable.key_signature,
          Color.Green
        ),
        QuizDescriptorDisplay(
          QuizDescriptor(QuizType.INTERVAL, "Intervals"),
          R.drawable.interval,
          Color.Blue
        ),
        QuizDescriptorDisplay(
          QuizDescriptor(QuizType.KEY, "Keys"),
          R.drawable.key_signature,
          Color.Green
        ),
      )
    ), {}) {}
  }

}