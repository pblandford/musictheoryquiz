package com.philblandford.musictheory.engine

import android.util.Log
import com.philblandford.kscore.engine.duration.Chord
import com.philblandford.kscore.engine.duration.Note
import com.philblandford.kscore.engine.duration.semibreve
import com.philblandford.kscore.engine.pitch.Harmony
import com.philblandford.kscore.engine.pitch.getSharps
import com.philblandford.kscore.engine.pitch.harmony
import com.philblandford.kscore.engine.types.Accidental
import com.philblandford.kscore.engine.types.ClefType
import com.philblandford.kscore.engine.types.NoteLetter
import com.philblandford.kscore.engine.types.Pitch
import com.philblandford.musictheory.R
import com.philblandford.musictheory.ui.longName
import com.philblandford.musictheory.ui.qualityFull

class TriadQuiz : Quiz() {

  private val questionString = stringResolver.getString(R.string.triad_question)

  override fun createQuestion(): Question? {
    val clefType = getClef()
    val triads = getTriads(clefType)
    val correctIdx = randomNumber.getInt(0, 3)
    val correct = triads[correctIdx]
    val octave = getOctave(clefType)
    val notes = correct.pitches().map {
      Note(
        semibreve(),
        it.copy(octave = octave, showAccidental = it.accidental != Accidental.NATURAL)
      )
    }
    val chord = Chord(semibreve(), notes)
    return standaloneGenerator.getChord(chord, clefType)?.let { standalone ->
      return Question(questionString, triads.map { it.fullName() }, correctIdx, standalone)
    }
  }

  private fun getTriads(clefType: ClefType): List<Harmony> {
    val triads = mutableListOf<Harmony>()

    while (triads.size < 4) {
      getRandomTriad(clefType)?.let { triad ->
        if (!triads.contains(triad)) {
          triads.add(triad)
        }
      }
    }
    return triads
  }

  private fun getRandomTriad(clefType: ClefType): Harmony? {
    val note = getRandomNote(clefType, ignore).let { it.copy(pitch = it.pitch.adjustOctaveSmallRange(clefType))}
    val quality = if (randomNumber.getInt(0, 2) == 0) "" else "m"
    return harmony("${note.pitch.letterString()}$quality")
  }

  companion object {
    fun triadQuiz() = TriadQuiz().apply { init() }
  }

  private val ignore = setOf(
    Pitch(NoteLetter.A, Accidental.SHARP),
    Pitch(NoteLetter.B, Accidental.SHARP),
    Pitch(NoteLetter.C, Accidental.FLAT),
    Pitch(NoteLetter.C, Accidental.SHARP),
    Pitch(NoteLetter.D, Accidental.SHARP),
    Pitch(NoteLetter.E, Accidental.SHARP),
    Pitch(NoteLetter.F, Accidental.FLAT),
    Pitch(NoteLetter.G, Accidental.SHARP),

  )

  private fun Harmony.fullName():String {
    return "${tone.letterString()} ${quality.qualityFull()}"
  }
}