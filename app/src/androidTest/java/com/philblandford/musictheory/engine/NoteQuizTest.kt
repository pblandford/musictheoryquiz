package com.philblandford.musictheory.engine

import com.philblandford.musictheory.TestBase
import com.philblandford.musictheory.engine.NoteQuiz.Companion.noteQuiz
import com.philblandford.musictheory.resources.DefaultRandom
import com.philblandford.musictheory.resources.RandomNumber
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class NoteQuizTest : TestBase() {

  private lateinit var noteQuiz: NoteQuiz

  override fun getRandom(): RandomNumber {
    return DefaultRandom
  }

  @Override
  override fun setup() {
    super.setup()
    noteQuiz = noteQuiz()
  }

  @Test
  fun testGetNoteLevel0() {
    val question = noteQuiz.getNextQuestion()
    assert(question?.correctAnswer?.length == 1)
  }

  @Test
  fun testGetNoteLevel1() {
    noteQuiz.setQuizLevel(1)
    val questions = (1..10).mapNotNull { noteQuiz.getNextQuestion() }
    assert(questions.any { it.correctAnswer.length > 1 })
  }

}