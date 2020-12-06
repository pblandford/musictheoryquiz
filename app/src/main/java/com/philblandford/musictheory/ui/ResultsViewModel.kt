package com.philblandford.musictheory.ui

import com.philblandford.musictheory.engine.Quiz

class ResultsViewModel : BaseViewModel<Quiz>() {

  override fun initModel(): Quiz? {
    return null
  }

  override fun doHandleIntent(model: Quiz, intent: UIIntent): Quiz {
    return model
  }

  fun getQuiz() = repository.getQuiz()
}