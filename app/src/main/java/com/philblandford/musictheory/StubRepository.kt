package com.philblandford.musictheory

import com.philblandford.musictheory.engine.IntervalQuiz.Companion.intervalQuiz
import com.philblandford.musictheory.engine.KeySignatureQuiz
import com.philblandford.musictheory.engine.KeySignatureQuiz.Companion.keySignatureQuiz
import com.philblandford.musictheory.engine.NoteQuiz.Companion.noteQuiz
import com.philblandford.musictheory.engine.Question
import com.philblandford.musictheory.engine.Quiz
import com.philblandford.musictheory.engine.ScaleQuiz.Companion.scaleQuiz
import com.philblandford.musictheory.engine.TermsQuiz.Companion.termsQuiz
import com.philblandford.musictheory.engine.TriadQuiz.Companion.triadQuiz
import com.philblandford.musictheory.resources.QuizDescriptor
import com.philblandford.musictheory.resources.QuizType
import com.philblandford.musictheory.resources.Repository

class StubRepository : Repository {

  private var currentQuiz: Quiz? = null

  override fun getNextQuestion(): Question? {
    return currentQuiz?.getNextQuestion()
  }

  override fun selectAnswer(idx: Int): Boolean {
    return currentQuiz?.selectAnswer(idx) ?: false
  }

  override fun getScore(): Int {
    return currentQuiz?.currentScore ?: 0
  }

  override fun getNumQuestions(): Int {
    return currentQuiz?.numQuestions ?: 0
  }

  override fun getQuestionsComplete(): Int {
    return currentQuiz?.questionsComplete() ?: 0
  }

  override fun setQuiz(quizType: QuizType) {
    currentQuiz = when (quizType) {
      QuizType.KEY -> keySignatureQuiz()
      QuizType.NOTE -> noteQuiz()
      QuizType.TERMS -> termsQuiz()
      QuizType.TRIAD -> triadQuiz()
      QuizType.INTERVAL -> intervalQuiz()
      QuizType.SCALE -> scaleQuiz()
    }
  }

  override fun getQuiz(): Quiz? {
    return currentQuiz
  }

  override fun clear() {
    currentQuiz = null
  }

  override fun getQuizzes(): List<QuizDescriptor> {
    return listOf(
      QuizDescriptor(QuizType.NOTE, "Notes"),
      QuizDescriptor(QuizType.KEY, "Key Signatures"),
      QuizDescriptor(QuizType.TERMS, "Musical Terms"),
      QuizDescriptor(QuizType.TRIAD, "Triads"),
      QuizDescriptor(QuizType.INTERVAL, "Intervals"),
      QuizDescriptor(QuizType.SCALE, "Scales")
    )
  }
}
