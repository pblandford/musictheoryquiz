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
import androidx.compose.ui.layout.WithConstraints
import androidx.compose.ui.layout.WithConstraintsScope
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.viewModel
import androidx.compose.ui.window.Popup
import com.philblandford.musictheory.resources.QuizType
import com.philblandford.musictheory.R
import com.philblandford.musictheory.engine.Level

@Composable
fun ChooseQuiz(onComplete: () -> Unit) {

  val viewModel = viewModel<ChooseQuizViewModel>()

  viewModel.getModel().observeAsState().value?.let { model ->

    WithConstraints(Modifier.fillMaxSize()) {

      ConstraintLayout(Modifier.fillMaxSize()) {
        val block = block()
        val (title, clefs, selection) = createRefs()
        Title(Modifier.constrainAs(title) {
          top.linkTo(parent.top, 10.dp)
          start.linkTo(parent.start)
          end.linkTo(parent.end)
          height = Dimension.value(block * 2)
          width = Dimension.fillToConstraints
        })

        Selection(model.quizzes, Modifier.constrainAs(selection) {
          top.linkTo(title.bottom, 10.dp)
          bottom.linkTo(clefs.top)
          start.linkTo(parent.start)
          end.linkTo(parent.end)
          width = Dimension.fillToConstraints
          height = Dimension.fillToConstraints
        }, { intent ->
          viewModel.receiveIntent(intent)
        }) { onComplete() }

        ClefSelection(model, viewModel, Modifier.constrainAs(clefs) {
          bottom.linkTo(parent.bottom, 10.dp)
          start.linkTo(parent.start)
          end.linkTo(parent.end)
          width = Dimension.fillToConstraints
        }, block())
      }
    }
  }

}

@Composable
private fun ClefSelection(
  model: ChooseQuizModel, viewModel: ChooseQuizViewModel,
  modifier: Modifier, height: Dp
) {

  Row(
    modifier,
    horizontalArrangement = Arrangement.SpaceEvenly,
    verticalAlignment = Alignment.CenterVertically
  ) {
    model.clefs.forEach { cd ->
      Checkbox(cd.enabled, onCheckedChange = { yes ->
        viewModel.receiveIntent(UIIntent.SelectClef(cd.clefType, yes))
      })
      Image(
        imageResource(cd.resId), Modifier.size(height),
        colorFilter = ColorFilter.tint(MaterialTheme.colors.onSecondary)
      )
    }
  }
}

@Composable
private fun Title(modifier: Modifier) {
  Box(modifier.background(MaterialTheme.colors.primary).border(2.dp, Color.White)) {
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
      cmd(UIIntent.SetLevel(level))
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


  ConstraintLayout(modifier.background(quizDescriptor.color).clickable(onClick = {
    select(quizDescriptor.quizDescriptor.type)
  })) {
    val (image, text) = createRefs()
    Image(
      imageResource(id = quizDescriptor.resId),
      Modifier.constrainAs(image) {
        centerTo(parent)
        width = Dimension.percent(0.5f)
        height = Dimension.percent(0.5f)
      }.background(Color.Transparent),
      colorFilter = ColorFilter.tint(MaterialTheme.colors.onSecondary)
    )
    Text(quizDescriptor.quizDescriptor.name, Modifier.constrainAs(text) {
      bottom.linkTo(parent.bottom, 10.dp)
      centerHorizontallyTo(parent)
    }, color = MaterialTheme.colors.onSecondary)
  }

}

@Composable
private fun LevelPopup(quizName: String, dismiss: () -> Unit, onSelect: (Int) -> Unit) {
  Popup(Alignment.Center, isFocusable = true, onDismissRequest = dismiss) {
    ChooseLevel(quizName, Modifier.fillMaxWidth(), onSelect)
  }
}
