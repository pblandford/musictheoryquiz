package com.philblandford.musictheory.ui

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.philblandford.kscore.engine.core.representation.Standalone
import com.philblandford.kscore.engine.types.ClefType
import com.philblandford.musictheory.R
import com.philblandford.musictheory.engine.Level
import com.philblandford.musictheory.resources.QuizDescriptor
import com.philblandford.musictheory.resources.QuizType




data class ChooseLevelModel(val levels:List<Level>)

class ChooseLevelViewModel : BaseViewModel<ChooseLevelModel>() {

  init {
    init()
  }

  fun getLevels():List<Level> = repository.getQuiz()?.getLevels() ?: listOf()

  override fun initModel(): ChooseLevelModel {
    return ChooseLevelModel(
      repository.getQuiz()?.getLevels() ?: listOf())
  }

}
