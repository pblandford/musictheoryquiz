package com.philblandford.musictheory.engine

import com.philblandford.kscore.engine.duration.Chord
import com.philblandford.kscore.engine.duration.semibreve
import com.philblandford.kscore.engine.types.*
import com.philblandford.musictheory.R
import com.philblandford.musictheory.ui.longName
import com.philblandford.musictheory.ui.name

class NoteQuiz private constructor (num:Int = DEFAULT_NUM_QUESTIONS) : Quiz(num) {

  override fun createQuestion(): Question {
    val clef = getClef()
    val chord = nextChord(clef)
    val standalone = standaloneGenerator.getChord(chord, clef)!!
    val allChords = getAnswerChords(chord, clef)
    val idx = allChords.indexOf(chord)
    val answers = allChords.map { it.name() }
    val question = stringResolver.getString(R.string.note_question)

    return Question(question, answers, idx, standalone)
  }

  private fun getAnswerChords(answer: Chord, clef: ClefType): List<Chord> {
    val answers = mutableListOf(answer)
    while (answers.size < 4) {
      val next = nextChord(clef)
      if (!answers.any { it.notes.first().pitch.noteLetter == next.notes.first().pitch.noteLetter &&
            it.notes.first().pitch.accidental == next.notes.first().pitch.accidental}) {
        answers.add(next)
      }
    }
    return randomNumber.randomList(answers)
  }

  private fun nextChord(clefType: ClefType): Chord {
    val note = getRandomNote(clefType)
    return Chord(semibreve(), listOf(note))
  }


  companion object {
    fun noteQuiz(num:Int = DEFAULT_NUM_QUESTIONS)  = NoteQuiz(num).apply { init() }
  }
}

