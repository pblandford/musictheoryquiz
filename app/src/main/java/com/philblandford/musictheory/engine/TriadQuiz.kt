package com.philblandford.musictheory.engine

import android.util.Log
import com.philblandford.kscore.engine.duration.Chord
import com.philblandford.kscore.engine.duration.Note
import com.philblandford.kscore.engine.duration.semibreve
import com.philblandford.kscore.engine.pitch.*
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
    val notes = correct.pitches(startOffset = getInversion()).randomise(clefType).map {
      Note(
        semibreve(),
        it.copy(showAccidental = it.accidental != Accidental.NATURAL)
      )
    }
    val chord = Chord(semibreve(), notes)
    return standaloneGenerator.getChord(chord, clefType)?.let { standalone ->
      return Question(questionString, triads.map { it.fullName() }, correctIdx, standalone)
    }
  }

  override fun getLevels(): List<Level> {
    return listOf(R.string.triad_level0, R.string.triad_level1, R.string.triad_level2).withIndex()
      .map {
        Level(it.index, stringResolver.getString(it.value))
      }
  }

  override fun getNoteRange(clefType: ClefType): List<Pitch> {
    return getClef(clefType)?.let { clef ->
      val range = -13..-4
      range.map { Pitch(clef.topNote, octave = clef.topNoteOctave).getNoteShift(it) }
    } ?: listOf()
  }

  private fun getInversion(): Int {
    return when (level) {
      0 -> 0
      else -> randomNumber.getInt(0, 3)
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
    val major = randomNumber.getInt(0, 2) == 0
    val note = getRandomNote(
      clefType,
      ignore(major)
    ).let { it.copy(pitch = it.pitch.adjustOctaveSmallRange(clefType)) }
    val quality = if (major) "" else "m"
    return harmony(
      "${note.pitch.letterString()}$quality",
      octave = note.pitch.octave
    )
  }

  private fun List<Pitch>.randomise(clefType: ClefType): List<Pitch> {
    return if (level == 2) {
      map { pitch ->
        val alt = randomNumber.getInt(-1, 2)
        val altered = pitch.copy(octave = pitch.octave + alt)
        if (altered.inRange(clefType)) altered else pitch
      }
    } else {
      this
    }
  }

  private fun Pitch.inRange(clefType: ClefType): Boolean {
    return getClef(clefType)?.let { clef ->
      val top = Pitch(clef.topNote, octave = clef.topNoteOctave)
      val pos = getNotePosition(top, this)
      pos in (-3..10)
    } ?: false
  }

  companion object {
    fun triadQuiz() = TriadQuiz().apply { init() }
  }

  private fun ignore(major: Boolean): Set<Pitch> {
    val base = setOf(
      Pitch(NoteLetter.A, Accidental.SHARP),
      Pitch(NoteLetter.B, Accidental.SHARP),
      Pitch(NoteLetter.C, Accidental.FLAT),
      Pitch(NoteLetter.C, Accidental.SHARP),
      Pitch(NoteLetter.D, Accidental.SHARP),
      Pitch(NoteLetter.E, Accidental.SHARP),
      Pitch(NoteLetter.F, Accidental.FLAT),
      Pitch(NoteLetter.G, Accidental.SHARP),
      )
    return if (major) base else base + Pitch(NoteLetter.G, Accidental.FLAT)
  }


  private fun Harmony.fullName(): String {
    return "${tone.letterString()} ${quality.qualityFull()}"
  }
}