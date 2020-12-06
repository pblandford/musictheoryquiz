package com.philblandford.musictheory.engine

import android.util.Log
import com.philblandford.kscore.engine.types.Accidental
import com.philblandford.kscore.engine.types.Accidental.*
import com.philblandford.kscore.engine.types.NoteLetter
import com.philblandford.kscore.engine.types.Pitch
import java.util.*

enum class IntervalAlteration{
  MAJOR, MINOR, PERFECT, DIMINISHED, AUGMENTED
}

data class Interval(val num:Int, val alteration:IntervalAlteration) {
  override fun toString(): String {
    return "${alteration.toString().toLowerCase(Locale.getDefault())} ${num.ordinal()}"
  }
}

private fun Int.ordinal():String {
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

fun Int.possibleAlterations():List<IntervalAlteration> {
  return when (this) {
    2,3,6,7 -> listOf(IntervalAlteration.MAJOR, IntervalAlteration.MINOR)
    4,5 -> listOf(IntervalAlteration.PERFECT, IntervalAlteration.DIMINISHED, IntervalAlteration.AUGMENTED)
    else -> listOf()
  }
}

fun Pitch.getInterval(interval: Interval):Pitch {
  var res = getGap(interval.num)
  res = res.getAlteration(interval, res.noteLetter, noteLetter)
  //Log.e("INT", "$this $interval $res")
  return res.adjustOctave()
}

private fun Pitch.getGap(num:Int):Pitch {
  val ordinal = noteLetter.ordinal + num - 1
  val newNoteLetter = noteLetter + num
  val newOctave = if (ordinal >= 7)  octave + 1 else octave
  return copy(noteLetter = newNoteLetter, octave = newOctave)
}

private fun Pitch.getAlteration(interval: Interval,
                                thisNoteLetter: NoteLetter,
                                originalNoteLetter: NoteLetter):Pitch {
  val newAccidental = when(interval.alteration) {
    IntervalAlteration.MAJOR -> accidental.getMajor(interval.num, thisNoteLetter, originalNoteLetter)
    IntervalAlteration.MINOR -> accidental.getMinor(interval.num, thisNoteLetter, originalNoteLetter)
    IntervalAlteration.AUGMENTED -> accidental.getAugmented(interval.num, thisNoteLetter, originalNoteLetter)
    IntervalAlteration.DIMINISHED -> accidental.getDiminished(thisNoteLetter)
    IntervalAlteration.PERFECT -> accidental.getPerfect(thisNoteLetter, originalNoteLetter)
  }
  return copy(accidental = newAccidental)
}


private operator fun NoteLetter.plus(num:Int):NoteLetter {
  val newOrdinal = ordinal + num - 1
  return NoteLetter.values()[newOrdinal%7]
}

private operator fun NoteLetter.minus(num:Int):NoteLetter {
  val newOrdinal = ordinal - num - 1
  return NoteLetter.values()[newOrdinal%7]
}

private fun Accidental.getMajor(num:Int, noteLetter: NoteLetter, originalNoteLetter: NoteLetter):Accidental {
  val thinner =  (noteLetter.isAwkwardSecond() || originalNoteLetter.isAwkward())
  return when (this) {
    NATURAL -> if (num == 7) {
      if (noteLetter.isAwkwardSecond() || noteLetter.isAwkward()) NATURAL else SHARP
    } else {
      if (thinner) SHARP else NATURAL
    }
    SHARP -> if (num == 7) {
      if (noteLetter.isAwkward()) SHARP else DOUBLE_SHARP
    } else {
      if (thinner) DOUBLE_SHARP else SHARP
    }
    FLAT -> if (thinner) NATURAL else FLAT
    else -> this
  }
}

private fun Accidental.getMinor(num: Int, noteLetter: NoteLetter, originalNoteLetter: NoteLetter):Accidental {
  return when (this) {
    NATURAL ->
      if (num == 7) {
        if (noteLetter.isAwkward()) FLAT else NATURAL
      } else {
        if (noteLetter.isAwkwardSecond() || originalNoteLetter.isAwkward()) NATURAL else FLAT
      }
    SHARP ->
      if (noteLetter.isAwkwardSecond()) SHARP else NATURAL

    FLAT ->
      if (num == 7) {
        FLAT
      } else {
        if (noteLetter.isAwkwardSecond() || originalNoteLetter.isAwkward()) FLAT else DOUBLE_FLAT
      }
    else -> this
  }
}

private fun Accidental.getPerfect(noteLetter: NoteLetter, originalNoteLetter: NoteLetter):Accidental {
  return when (this) {
    NATURAL -> if (noteLetter.isAwkwardSecond() && originalNoteLetter.isAwkward()) SHARP else NATURAL
    SHARP -> if (noteLetter.isAwkward() || originalNoteLetter.isAwkward()) NATURAL else SHARP
    FLAT -> if (originalNoteLetter.isAwkward() && !noteLetter.isAwkward()) NATURAL else FLAT
    else -> this
  }
}

private fun Accidental.getAugmented(num:Int, noteLetter: NoteLetter, originalNoteLetter: NoteLetter):Accidental {
  return when (this) {
    NATURAL -> if (num.isPerfect()) {
      if (originalNoteLetter.isAwkward() && noteLetter.isAwkwardSecond()) DOUBLE_SHARP else SHARP
    } else {
      if (originalNoteLetter.isAwkward()) DOUBLE_SHARP else SHARP
    }
    SHARP -> if (noteLetter.isAwkward()) SHARP else DOUBLE_SHARP
    FLAT ->
        if (originalNoteLetter.isAwkward() && noteLetter.isAwkwardSecond()) SHARP else NATURAL

    else -> this
  }
}

private fun Accidental.getDiminished(noteLetter: NoteLetter):Accidental {
  return when (this) {
    NATURAL -> FLAT
    SHARP -> if (noteLetter.isAwkward()) FLAT else NATURAL
    FLAT -> DOUBLE_FLAT
    else -> this
  }
}

private fun Pitch.adjustOctave():Pitch {
  val newOctave = when (noteLetter to accidental) {
    NoteLetter.B to SHARP -> octave + 1
    NoteLetter.C to FLAT -> octave - 1
    else -> octave
  }
  return copy(octave = newOctave)
}


private fun NoteLetter.isAwkward() = this == NoteLetter.B || this == NoteLetter.E

private fun NoteLetter.isAwkwardSecond() = this == NoteLetter.C || this == NoteLetter.F

private fun Int.isPerfect() = this == 4 || this == 5