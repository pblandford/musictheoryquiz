package com.philblandford.musictheory.ui

import com.philblandford.kscore.engine.types.ClefType
import com.philblandford.musictheory.resources.QuizType

sealed class UIIntent {
  data class SelectQuiz(val quizType: QuizType) : UIIntent()
  data class SetLevel(val level:Int) : UIIntent()
  data class SelectAnswer(val idx:Int) : UIIntent()
  object NextQuestion : UIIntent()
  data class SelectClef(val clefType: ClefType, val yes:Boolean) : UIIntent()
  object QuizComplete: UIIntent()
  object ClearCompleteFlag : UIIntent()
}

