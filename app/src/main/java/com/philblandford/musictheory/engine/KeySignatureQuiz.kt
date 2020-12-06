package com.philblandford.musictheory.engine

import com.philblandford.kscore.api.KScoreImpl
import com.philblandford.kscore.engine.types.*
import com.philblandford.musictheory.R
import kotlin.random.Random

class KeySignatureQuiz private constructor (num:Int = DEFAULT_NUM_QUESTIONS) : Quiz(num) {

  override fun createQuestion(): Question {
    val ks = nextKs()
    val standalone = standaloneGenerator.getHeader(clef = ClefType.TREBLE, ks = ks)!!
    val allKs = getAnswerNumbers(ks)
    val idx = allKs.indexOf(ks)
    val answers = allKs.map { keyNames[it] ?: error("") }.map {
      "$it Major"
    }
    val question = stringResolver.getString(R.string.ks_question)

    return Question(question, answers, idx, standalone)
  }

  private fun getAnswerNumbers(answer: Int): List<Int> {
    val answers = mutableListOf(answer)
    while (answers.size < 4) {
      val next = randomNumber.getInt(-7, 7)
      if (!answers.contains(next)) {
        answers.add(next)
      }
    }
    return randomNumber.randomList(answers)
  }

  private fun nextKs(): Int {
    return randomNumber.getInt(-7, 7)
  }

  companion object {
    fun keySignatureQuiz(num:Int = DEFAULT_NUM_QUESTIONS)  = KeySignatureQuiz(num).apply { init() }
  }
}

