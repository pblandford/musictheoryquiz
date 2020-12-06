package com.philblandford.musictheory.engine

import com.philblandford.kscore.engine.duration.Chord
import com.philblandford.kscore.engine.duration.Note
import com.philblandford.kscore.engine.duration.semibreve
import com.philblandford.kscore.engine.pitch.getScale
import com.philblandford.kscore.engine.types.Accidental
import com.philblandford.kscore.engine.types.ClefType
import com.philblandford.kscore.engine.types.NoteLetter
import com.philblandford.kscore.engine.types.Pitch
import com.philblandford.musictheory.R

data class ScaleDescriptor(val tonic: Pitch, val major: Boolean)

class ScaleQuiz : Quiz() {

  override fun createQuestion(): Question? {
    val clef = getClef()
    val tonic = getRandomNote(clef, ignorables)
    val scale = getScale(tonic.pitch, false, clef)
    return standaloneGenerator.getChords(scale, clef)?.let { standalone ->
      val answers = getAnswers(clef, tonic.pitch)
      val correctIdx = answers.indexOf(tonic.pitch.octaveless())
      Question(
        stringResolver.getString(R.string.scale_question),
        answers.map { getScaleName(it, false) }, correctIdx, standalone
      )
    }
  }

  private fun getScale(pitch: Pitch, minor: Boolean, clefType: ClefType): List<Chord> {
    val adjusted = pitch.adjustOctaveSmallRange(clefType)
    val pitches =
      adjusted.getScale(minor).map { it.copy(showAccidental = it.accidental != Accidental.NATURAL) }
        .shuffled()
    return pitches.map { p ->
      Chord(semibreve(), listOf(Note(semibreve(), p)))
    }
  }

  private fun List<Pitch>.randomise(): List<Pitch> {
    val start = randomNumber.getInt(0, 7)
    return drop(start) + take(start)
  }

  private fun getAnswers(clefType: ClefType, correct: Pitch): List<Pitch> {
    val answers = mutableListOf(correct.octaveless())
    while (answers.size < 4) {
      val next = getRandomNote(clefType, ignorables)
      if (!answers.contains(next.pitch.octaveless())) {
        answers.add(next.pitch.octaveless())
      }
    }
    return answers
  }

  private val ignorables = setOf(
    Pitch(NoteLetter.D, Accidental.SHARP),
    Pitch(NoteLetter.E, Accidental.SHARP),
    Pitch(NoteLetter.F, Accidental.FLAT),
    Pitch(NoteLetter.G, Accidental.SHARP),
    Pitch(NoteLetter.A, Accidental.SHARP),
    Pitch(NoteLetter.B, Accidental.SHARP),
  )

  private fun getScaleName(tonic: Pitch, minor: Boolean):String {
    return "${tonic.letterString()} ${if (minor) "Minor" else "Major"}"
  }

  companion object {
    fun scaleQuiz() = ScaleQuiz().apply { init() }
  }
}