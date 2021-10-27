package com.philblandford.musictheory.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.philblandford.musictheory.engine.Question
import com.philblandford.musictheory.resources.Repository
import com.philblandford.musictheory.resources.SoundPlayer
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

data class QuestionModel(
  val question: Question?,
  val answers:List<Boolean>,
  val numQuestions:Int,
  val highlightAnswer:Boolean = false,
  val allCorrect: Boolean = true,
  val complete: Boolean = false
)

class QuestionViewModel : BaseViewModel<QuestionModel>(), KoinComponent {

  private val soundPlayer: SoundPlayer by inject()

  init {
    init()
  }

  fun startQuiz() {
    if (getModel().value?.complete == false && getModel().value?.question == null) {
      init()
    }
  }

  override fun initModel(): QuestionModel {
    return QuestionModel(
      repository.getNextQuestion(), listOf(), repository.getNumQuestions(),
      false,
      repository.getScore() == repository.getQuestionsComplete()
    )
  }

  override fun doHandleIntent(model: QuestionModel, intent: UIIntent): QuestionModel {

    return when (intent) {
      is UIIntent.SelectAnswer -> {
        var nextQuestion = model.question
        val answer = repository.selectAnswer(intent.idx)
        if (answer) {
          soundPlayer.playSound("correct")
          nextQuestion = repository.getNextQuestion()
        } else {
          soundPlayer.playSound("wrong")
        }
        QuestionModel(
          nextQuestion, model.answers.plus(answer), model.numQuestions,
          !answer,
          repository.getScore() == repository.getQuestionsComplete(),
          nextQuestion == null
        )
      }
      is UIIntent.NextQuestion -> {
        val nextQuestion = repository.getNextQuestion()
        QuestionModel(
          nextQuestion, model.answers, model.numQuestions, false,
          repository.getScore() == repository.getQuestionsComplete(),
          nextQuestion == null
        )
      }
      UIIntent.ClearCompleteFlag -> {
        model.copy(complete = false)
      }
      else -> model
    }
  }
}
