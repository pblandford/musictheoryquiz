package com.philblandford.musictheory.engine

import com.philblandford.kscore.api.*
import com.philblandford.kscore.engine.core.representation.Standalone
import com.philblandford.kscore.engine.core.representation.StandaloneGenerator
import com.philblandford.kscore.engine.duration.Note
import com.philblandford.kscore.engine.duration.semibreve
import com.philblandford.kscore.engine.pitch.getClef
import com.philblandford.kscore.engine.types.*
import com.philblandford.musictheory.resources.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*
import kotlin.random.Random

const val DEFAULT_NUM_QUESTIONS = 10

data class Question(val text:String, val answers:List<String>, val correctIdx:Int, val standalone:Standalone) {
  val correctAnswer = answers[correctIdx]
}

abstract class Quiz(val numQuestions:Int = DEFAULT_NUM_QUESTIONS) : KoinComponent {

  var currentScore:Int = 0

  protected val stringResolver:StringResolver by inject()
  protected val randomNumber:RandomNumber by inject()

  private val drawableGetter:DrawableGetter by inject()
  protected val standaloneGenerator = StandaloneGenerator(drawableGetter)
  protected val assetManager:AssetManager by inject()
  protected val preferenceGetter:PreferenceGetter by inject()

  private var currentQuestion:Question? = null
  private val questions = Stack<Question>()

  protected abstract fun createQuestion():Question?

  fun getNextQuestion():Question? {
    currentQuestion = try {
      questions.pop()
    } catch (e:EmptyStackException) {
      null
    }
    return currentQuestion
  }

  fun selectAnswer(idx:Int):Boolean {
    return currentQuestion?.let { q ->
      if (q.correctIdx == idx) {
        currentScore += 1
        true
      } else {
        false
      }
    } ?: false
  }

  fun questionsComplete():Int {
    return numQuestions - questions.size - 1
  }

  fun init() {
    while(questions.size < numQuestions) {
      createQuestion()?.let { next ->
        if (!questions.any { it.correctAnswer == next.correctAnswer }) {
          questions.push(next)
        }
      }
    }
  }

  protected fun getClef():ClefType {
    val clefs = preferenceGetter.getPreferredClefs()
    return clefs[randomNumber.getInt(0, clefs.size)]
  }

  protected fun getOctave(clefType:ClefType):Int {
    val clef = getClef(clefType)!!
    return clef.topNoteOctave + randomNumber.getInt(-1,1)
  }

  protected fun getRandomNote(clefType: ClefType, ignore:Set<Pitch> = setOf()): Note {
    var pitch:Pitch? = null
    val ignoreNoOctave = ignore.map { it.octaveless() }

    while (pitch == null || ignoreNoOctave.contains(pitch.octaveless())) {

      val letter = NoteLetter.values()[randomNumber.getInt(0, 7)]
      val accidental = accidentals[randomNumber.getInt(0, accidentals.size)]
      val octave = getOctave(clefType)
      pitch = Pitch(letter, accidental, octave, accidental != Accidental.NATURAL)
    }
    return Note(semibreve(), pitch)
  }

  protected fun Pitch.adjustOctaveSmallRange(clef: ClefType):Pitch {
    val octave = when (clef) {
      ClefType.TREBLE -> 4
      ClefType.ALTO -> 3
      ClefType.BASS -> 2
      ClefType.TENOR -> 3
      else -> 4
    }
    return copy(octave = octave)
  }

  private val accidentals = listOf(Accidental.SHARP, Accidental.FLAT, Accidental.NATURAL)
}