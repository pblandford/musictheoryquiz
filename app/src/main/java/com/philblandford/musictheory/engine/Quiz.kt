package com.philblandford.musictheory.engine

import android.util.Log
import com.philblandford.kscore.api.*
import com.philblandford.kscore.engine.core.representation.Standalone
import com.philblandford.kscore.engine.core.representation.StandaloneGenerator
import com.philblandford.kscore.engine.duration.Note
import com.philblandford.kscore.engine.duration.semibreve
import com.philblandford.kscore.engine.pitch.getClef
import com.philblandford.kscore.engine.pitch.getNoteShift
import com.philblandford.kscore.engine.types.*
import com.philblandford.musictheory.resources.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*
import kotlin.random.Random

const val DEFAULT_NUM_QUESTIONS = 10

data class Level(val num:Int, val description:String)

data class Question(
  val text: String,
  val answers: List<String>,
  val correctIdx: Int,
  val standalone: Standalone
) {
  val correctAnswer = answers[correctIdx]
}

abstract class Quiz(
  val numQuestions: Int = DEFAULT_NUM_QUESTIONS,
  protected var level: Int = 0
) : KoinComponent {

  var currentScore: Int = 0

  protected val stringResolver: StringResolver by inject()
  protected val randomNumber: RandomNumber by inject()

  private val drawableGetter: DrawableGetter by inject()
  protected val standaloneGenerator = StandaloneGenerator(drawableGetter)
  protected val assetManager: AssetManager by inject()
  protected val preferenceGetter: PreferenceGetter by inject()

  private var currentQuestion: Question? = null
  private val questions = Stack<Question>()

  protected abstract fun createQuestion(): Question?
  open fun getLevels():List<Level> = listOf()

  fun setQuizLevel(value:Int) {
    level = value
    init()
  }

  fun getNextQuestion(): Question? {
    currentQuestion = try {
      questions.pop()
    } catch (e: EmptyStackException) {
      null
    }
    return currentQuestion
  }

  fun selectAnswer(idx: Int): Boolean {
    return currentQuestion?.let { q ->
      if (q.correctIdx == idx) {
        currentScore += 1
        true
      } else {
        false
      }
    } ?: false
  }

  fun questionsComplete(): Int {
    return numQuestions - questions.size - 1
  }

  open fun init() {
    questions.clear()
    while (questions.size < numQuestions) {
      createQuestion()?.let { next ->
        if (!requireUnique() || !questions.any { it.correctAnswer == next.correctAnswer }) {
          questions.push(next)
        }
      }
    }
  }

  protected open fun requireUnique() = true

  protected fun getClef(): ClefType {
    val clefs = preferenceGetter.getPreferredClefs()
    return clefs[randomNumber.getInt(0, clefs.size)]
  }

  protected open fun getOctave(clefType: ClefType): Int {
    val clef = getClef(clefType)!!
    return clef.topNoteOctave + randomNumber.getInt(-1, 1)
  }

  protected fun getRandomNote(clefType: ClefType, ignore: Set<Pitch> = setOf()): Note {
    var pitch: Pitch? = null
    val ignoreNoOctave = ignore.map { it.octaveless() }
    val range = getNoteRange(clefType)

    while (pitch == null || ignoreNoOctave.contains(pitch.octaveless())) {
      pitch = range[randomNumber.getInt(0, range.size)]
      val accidental = getAccidental()
      pitch = pitch.copy(accidental = accidental, showAccidental = accidental != Accidental.NATURAL)
        .correctOctave()
    }
    return Note(semibreve(), pitch)
  }

  protected open fun getNoteRange(clefType: ClefType): List<Pitch> {
    return getClef(clefType)?.let { clef ->
      val range = if (!fullRange()) -13..1 else -24..12
      range.map { Pitch(clef.topNote, octave = clef.topNoteOctave).getNoteShift(it) }
    } ?: listOf()
  }

  protected open fun fullRange():Boolean {
    return true
  }

  protected open fun getAccidental():Accidental {
    return if (level == 0) {
      Accidental.NATURAL
    } else {
      accidentals[randomNumber.getInt(0, accidentals.size)]
    }
  }

  protected fun Pitch.adjustOctaveSmallRange(clef: ClefType): Pitch {
    val octave = when (clef) {
      ClefType.TREBLE -> 4
      ClefType.ALTO -> 3
      ClefType.BASS -> 2
      ClefType.TENOR -> 3
      else -> 4
    }
    return copy(octave = octave)
  }

  protected fun Pitch.correctOctave(): Pitch {
    return if (noteLetter == NoteLetter.B && accidental == Accidental.SHARP) {
      copy(octave = octave + 1)
    } else if (noteLetter == NoteLetter.C && accidental == Accidental.FLAT) {
      copy(octave = octave - 1)
    } else this
  }

  private val accidentals = listOf(Accidental.SHARP, Accidental.FLAT, Accidental.NATURAL)
}