package com.philblandford.musictheory.engine

import com.philblandford.musictheory.R
import java.util.*
import kotlin.random.Random

class TermsQuiz private constructor(num: Int = DEFAULT_NUM_QUESTIONS) : Quiz() {

  private val allTerms = assetManager.getTerms().toList()
  private val terms = allTerms.shuffled().take(num).toMutableList()

  private fun getAnswers(question: String, correct: String): Pair<List<String>, Int> {
    val wrong = allTerms.filterNot { it.first == question }.shuffled().take(3).map { it.second }
    val all = wrong.plus(correct).shuffled()
    val idx = all.indexOf(correct)
    return all to idx
  }

  override fun createQuestion(): Question? {
    return terms.firstOrNull()?.let { term ->
      terms.removeAt(0)
      val (answers, idx) = getAnswers(term.first, term.second)
      standaloneGenerator.getText(term.first, "expression")?.let { standalone ->
        Question(stringResolver.getString(R.string.term_question), answers, idx, standalone)
      }
    }
  }

  override fun init() {
    terms.clear()
    terms.addAll(allTerms.shuffled().take(numQuestions))
    super.init()
  }

  companion object {
    fun termsQuiz(num: Int = DEFAULT_NUM_QUESTIONS) = TermsQuiz(num).apply { init() }
  }

}