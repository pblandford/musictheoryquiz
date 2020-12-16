package com.philblandford.musictheory.engine

import com.philblandford.kscore.api.KScoreImpl
import com.philblandford.kscore.engine.types.*
import com.philblandford.musictheory.R
import kotlin.random.Random

class KeySignatureQuiz private constructor(num: Int = DEFAULT_NUM_QUESTIONS) : Quiz(num) {

  override fun createQuestion(): Question {
    val ks = nextKs()
    val standalone = standaloneGenerator.getHeader(clef = ClefType.TREBLE, ks = ks)!!
    val allKs = getAnswerNumbers(ks)
    val idx = allKs.indexOf(ks)
    val answers = allKs.map { sharps ->
      if (isMajor()) {
        "${majorKeyNames[sharps] ?: error("")} Major"
      } else {
        "${minorKeyNames[sharps] ?: error("")} Minor"
      }
    }

    val question = stringResolver.getString(R.string.ks_question)

    return Question(question, answers, idx, standalone)
  }

  override fun getLevels(): List<Level> {
    return listOf(
      Level(0, stringResolver.getString(R.string.key_level0)),
      Level(1, stringResolver.getString(R.string.key_level1)),
      Level(2, stringResolver.getString(R.string.key_level2)),
      Level(3, stringResolver.getString(R.string.key_level3)),
    )
  }

  override fun requireUnique(): Boolean {
    return level > 1
  }

  fun isMajor() = level == 0 || level == 2

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
    val accidentals = if (level > 1) 7 else 4
    return randomNumber.getInt(-accidentals, accidentals)
  }

  companion object {
    fun keySignatureQuiz(num: Int = DEFAULT_NUM_QUESTIONS) = KeySignatureQuiz(num).apply { init() }
  }
}

