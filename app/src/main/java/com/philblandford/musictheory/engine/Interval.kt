package com.philblandford.musictheory.engine

import com.philblandford.kscore.engine.types.Accidental
import com.philblandford.kscore.engine.types.Accidental.*
import com.philblandford.kscore.engine.types.NoteLetter
import com.philblandford.kscore.engine.types.Pitch
import java.util.*

enum class IntervalAlteration {
  MAJOR, MINOR, PERFECT, DIMINISHED, AUGMENTED
}

data class Interval(val num: Int, val alteration: IntervalAlteration) {
  override fun toString(): String {
    return "${alteration.toString().toLowerCase(Locale.getDefault())} ${num.ordinal()}"
  }
}

private data class GapDescr(
  val start: NoteLetter, val accidental: Accidental,
  val gap: Int
)

private fun Int.ordinal(): String {
  return when (this) {
    2 -> "2nd"
    3 -> "3rd"
    4 -> "4th"
    5 -> "5th"
    6 -> "6th"
    7 -> "7th"
    8 -> "Octave"
    else -> ""
  }
}



fun Pitch.getInterval(interval: Interval): Pitch {
  var res = getGap(interval.num)
  res = res.getAlteration(interval, res.noteLetter, noteLetter)
  //Log.e("INT", "$this $interval $res")
  return res.adjustOctave()
}

private fun Pitch.getGap(num: Int): Pitch {
  val ordinal = noteLetter.ordinal + num - 1
  val newNoteLetter = noteLetter + num
  val newOctave = if (ordinal >= 7) octave + 1 else octave
  return copy(noteLetter = newNoteLetter, octave = newOctave)
}

private fun Pitch.getAlteration(
  interval: Interval,
  thisNoteLetter: NoteLetter,
  originalNoteLetter: NoteLetter
): Pitch {
  val newAccidental = when (interval.alteration) {
    IntervalAlteration.MAJOR -> accidental.getMajor(interval.num, originalNoteLetter)
    IntervalAlteration.MINOR -> accidental.getMinor(interval.num, originalNoteLetter)
    IntervalAlteration.AUGMENTED -> accidental.getAugmented(interval.num, originalNoteLetter)
    IntervalAlteration.DIMINISHED -> accidental.getDiminished(interval.num, originalNoteLetter)
    IntervalAlteration.PERFECT -> accidental.getPerfect(interval.num, originalNoteLetter)
  }
  return copy(accidental = newAccidental)
}


private operator fun NoteLetter.plus(num: Int): NoteLetter {
  val newOrdinal = ordinal + num - 1
  return NoteLetter.values()[newOrdinal % 7]
}

private operator fun NoteLetter.minus(num: Int): NoteLetter {
  val newOrdinal = ordinal - num - 1
  return NoteLetter.values()[newOrdinal % 7]
}

private fun Accidental.getMajor(num: Int, originalNoteLetter: NoteLetter): Accidental {
  return if ((num < 4 && semitonesCrossed(num, originalNoteLetter) >= 1) ||
    (num >= 6 && semitonesCrossed(num, originalNoteLetter) == 2)
  ) {
    upOne()
  } else {
    this
  }
}

private fun Accidental.getMinor(
  num: Int,
  originalNoteLetter: NoteLetter
): Accidental {
  return if ((num < 4 && semitonesCrossed(num, originalNoteLetter) >= 1) ||
    (num >= 6 && semitonesCrossed(num, originalNoteLetter) == 2)
  ) {
    this
  } else {
    downOne()
  }
}

private fun Accidental.getPerfect(
  num:Int,
  originalNoteLetter: NoteLetter
): Accidental {
  return if (originalNoteLetter == NoteLetter.B && num == 5) {
    upOne()
  } else if (originalNoteLetter == NoteLetter.F && num ==4) {
    downOne()
  } else {
    this
  }
}

private fun Accidental.getAugmented(
  num: Int,
  originalNoteLetter: NoteLetter
): Accidental {
  return if (originalNoteLetter == NoteLetter.F && num == 4) {
    this
  } else if (originalNoteLetter == NoteLetter.B && num ==5) {
    upTwo()
  } else if (num.isPerfect()) {
    upOne()
  } else if (num < 4 && semitonesCrossed(num, originalNoteLetter) == 1) {
    upTwo()
  } else {
    upOne()
  }
}

private fun Accidental.getDiminished(num:Int, originalNoteLetter: NoteLetter): Accidental {
  return if (originalNoteLetter == NoteLetter.F && num == 4) {
    downTwo()
  } else if (originalNoteLetter == NoteLetter.B && num ==5) {
    this
  } else if (num.isPerfect()) {
    downOne()
  } else if (num < 4 && semitonesCrossed(num, originalNoteLetter) == 1) {
    downTwo()
  } else {
    downOne()
  }
}

private fun Pitch.adjustOctave(): Pitch {
  val newOctave = when (noteLetter to accidental) {
    NoteLetter.B to SHARP -> octave + 1
    NoteLetter.C to FLAT -> octave - 1
    else -> octave
  }
  return copy(octave = newOctave)
}

private fun semitonesCrossed(num: Int, noteLetter: NoteLetter): Int {
  val notesSpanned = (NoteLetter.values() + NoteLetter.values()).toList().subList(
    noteLetter.ordinal + 1,
    noteLetter.ordinal + num
  )
  return notesSpanned.count { it == NoteLetter.C || it == NoteLetter.F }
}

private fun NoteLetter.isAwkward() = this == NoteLetter.B || this == NoteLetter.E

private fun NoteLetter.isAwkwardSecond() = this == NoteLetter.C || this == NoteLetter.F

private fun Int.isPerfect() = this == 4 || this == 5

private fun Accidental.upOne(): Accidental {
  return when (this) {
    NATURAL -> SHARP
    SHARP -> DOUBLE_SHARP
    FLAT -> NATURAL
    else -> this
  }
}

private fun Accidental.downOne(): Accidental {
  return when (this) {
    NATURAL -> FLAT
    SHARP -> NATURAL
    FLAT -> DOUBLE_FLAT
    else -> this
  }
}

private fun Accidental.downTwo(): Accidental {
  return when (this) {
    NATURAL -> DOUBLE_FLAT
    SHARP -> FLAT
    else -> this
  }
}

private fun Accidental.upTwo(): Accidental {
  return when (this) {
    NATURAL -> DOUBLE_SHARP
    FLAT -> SHARP
    else -> this
  }
}