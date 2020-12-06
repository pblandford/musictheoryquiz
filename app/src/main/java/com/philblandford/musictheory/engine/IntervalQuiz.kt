package com.philblandford.musictheory.engine

import android.util.Log
import androidx.compose.ui.graphics.colorspace.Illuminant.C
import com.philblandford.kscore.engine.duration.Chord
import com.philblandford.kscore.engine.duration.Note
import com.philblandford.kscore.engine.duration.semibreve
import com.philblandford.kscore.engine.types.*
import com.philblandford.musictheory.R

private data class IntervalDescriptor(val chord: Chord, val interval: Interval)

class IntervalQuiz private constructor (num:Int = DEFAULT_NUM_QUESTIONS) : Quiz(num) {

  override fun createQuestion(): Question {
    val clef = getClef()
    Log.e("INT", "CORRECT")
    val intervalDesc = nextChord(clef)
    val standalone = standaloneGenerator.getChord(intervalDesc.chord, clef)!!

    val allIntervals = getAnswerIntervals(intervalDesc, clef)
    val idx = allIntervals.indexOf(intervalDesc.interval)
    val answers = allIntervals.map { it.toString() }
    val question = stringResolver.getString(R.string.interval_question)

    return Question(question, answers, idx, standalone)
  }

  private fun getAnswerIntervals(answer: IntervalDescriptor, clef: ClefType): List<Interval> {
    val answers = mutableListOf(answer.interval)
    while (answers.size < 4) {
      val next = getRandomInterval()
      if (!answers.contains(next)) {
        answers.add(next)
      }
    }
    return randomNumber.randomList(answers)
  }

  private fun nextChord(clefType: ClefType): IntervalDescriptor {
    val note = getRandomNote(clefType, ignorables).let{ it.copy(pitch = it.pitch.adjustOctaveSmallRange(clefType))}
    val interval = getRandomInterval()
    val note2 = Note(semibreve(), note.pitch.getInterval(interval).let {
      it.copy(showAccidental = (it.accidental != Accidental.NATURAL)) })
    return IntervalDescriptor(Chord(semibreve(), listOf(note, note2)), interval)
  }

  private fun getRandomInterval():Interval {
    val num = randomNumber.getInt(2,8)
    val alterations = num.possibleAlterations()
    val alteration = alterations[randomNumber.getInt(0, alterations.size)]
    return Interval(num, alteration)
  }

  private val ignorables = setOf(
    Pitch(NoteLetter.C, Accidental.FLAT),
    Pitch(NoteLetter.B, Accidental.SHARP),
    Pitch(NoteLetter.E, Accidental.SHARP),
    Pitch(NoteLetter.F, Accidental.FLAT),
  )



  companion object {
    fun intervalQuiz(num:Int = DEFAULT_NUM_QUESTIONS)  = IntervalQuiz(num).apply { init() }
  }
}

