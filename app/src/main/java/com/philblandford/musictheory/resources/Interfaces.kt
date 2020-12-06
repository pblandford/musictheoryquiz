package com.philblandford.musictheory.resources

import com.philblandford.kscore.engine.types.ClefType
import com.philblandford.musictheory.engine.Question
import com.philblandford.musictheory.engine.Quiz
import kotlin.random.Random

enum class QuizType {
  KEY,
  NOTE,
  TERMS,
  TRIAD,
  INTERVAL,
  SCALE
}

data class QuizDescriptor(val type: QuizType, val name: String)

interface StringResolver {
  fun getString(id:Int):String
}

interface RandomNumber {
  fun getInt(start:Int, end:Int):Int
  fun <T>randomList(original:List<T>):List<T>
}

interface Repository {
  fun getNextQuestion(): Question?
  fun selectAnswer(idx:Int):Boolean
  fun getScore():Int
  fun getNumQuestions():Int
  fun getQuestionsComplete():Int
  fun setQuiz(quizType: QuizType)
  fun getQuiz():Quiz?
  fun getQuizzes():List<QuizDescriptor>
}

interface SoundPlayer {
  fun playSound(name:String)
}

interface AssetManager {
  fun getTerms():Map<String, String>
}

interface PreferenceGetter {
  fun allClefs():List<ClefType>
  fun setPreferredClefs(clefs:List<ClefType>)
  fun getPreferredClefs():List<ClefType>
}

object DefaultRandom : RandomNumber {
  override fun getInt(start: Int, end: Int): Int {
    return Random.nextInt(start, end)
  }

  override fun <T> randomList(original: List<T>): List<T> {
    return original.shuffled()
  }
}