package com.philblandford.musictheory.engine

import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.philblandford.musictheory.TestBase
import com.philblandford.musictheory.engine.KeySignatureQuiz.Companion.keySignatureQuiz
import com.philblandford.musictheory.resources.RandomNumber
import junit.framework.TestCase
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.containsInAnyOrder
import org.junit.Test
import java.util.*

class KeySignatureQuizTest : TestBase() {

  private val mockRandom:RandomNumber = mock()
  private val numberStack = Stack<Int>()
  private lateinit var keySignatureQuiz:KeySignatureQuiz

  @Override
  override fun setup() {
    super.setup()
    numberStack.clear()
  }

  override fun getRandom(): RandomNumber {
    whenever(mockRandom.randomList(any<List<String>>())).thenAnswer {
      it.arguments.first()
    }
    whenever(mockRandom.getInt(any(),any())).thenAnswer {
      numberStack.pop()
    }

    return mockRandom
  }

  @Test
  fun testGetQuestionAnswers() {
    numberStack.addAll(listOf(4,2,-3,6,4))
    keySignatureQuiz = getKsQuiz()
    val question = keySignatureQuiz.getNextQuestion()!!
    assertThat(question.answers, containsInAnyOrder("D Major", "Eb Major", "F# Major", "E Major"))
  }

  @Test
  fun testGetQuestionDuplicate() {
    numberStack.addAll(listOf(1,4,2,-3,2,6))
    keySignatureQuiz = getKsQuiz()

    val question = keySignatureQuiz.getNextQuestion()!!
    assertThat(question.answers, containsInAnyOrder("D Major", "Eb Major", "F# Major", "E Major"))
  }

  @Test
  fun testGetQuestionIdx() {
    numberStack.addAll(listOf(3,4,2,-3,6))
    keySignatureQuiz = getKsQuiz()

    val question = keySignatureQuiz.getNextQuestion()!!
    assertThat(question.correctIdx, `is`(question.answers.indexOf("F# Major")))
  }

  @Test
  fun testSelectCorrectAnswer() {
    numberStack.addAll(listOf(3,4,2,-3,6))
    keySignatureQuiz = getKsQuiz()

    val question = keySignatureQuiz.getNextQuestion()!!
    val answerIdx = question.answers.indexOfFirst { it == "F# Major" }
    val res = keySignatureQuiz.selectAnswer(answerIdx)
    assert(res)
    assertThat(keySignatureQuiz.currentScore, `is`(1))
  }

  
  private fun getKsQuiz() = keySignatureQuiz(1)
}