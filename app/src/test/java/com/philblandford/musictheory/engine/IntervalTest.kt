package com.philblandford.musictheory.engine

import com.philblandford.kscore.engine.types.Accidental.*
import com.philblandford.kscore.engine.types.NoteLetter.*
import com.philblandford.kscore.engine.types.Pitch
import  com.philblandford.musictheory.engine.IntervalAlteration.*
import org.junit.Assert.*
import org.junit.Test

class IntervalTest {

  @Test
  fun testGetMinorSecond() {
    val res = default.getInterval(Interval(2, MINOR))
    assertEquals(Pitch(D, FLAT, 4), res)
  }

  @Test
  fun testGetMajorSecond() {
    val res = default.getInterval(Interval(2, MAJOR))
    assertEquals(Pitch(D, NATURAL, 4), res)
  }

  @Test
  fun testGetAugmentedSecond() {
    val res = default.getInterval(Interval(2, AUGMENTED))
    assertEquals(Pitch(D, SHARP, 4), res)
  }

  @Test
  fun testGetAugmentedSecondG() {
    val res = Pitch(G, NATURAL,5).getInterval(Interval(2, AUGMENTED))
    assertEquals(Pitch(A, SHARP, 5), res)
  }

  @Test
  fun testGetMinorThird() {
    val res = default.getInterval(Interval(3, MINOR))
    assertEquals(Pitch(E, FLAT, 4), res)
  }

  @Test
  fun testGetMajorThird() {
    val res = default.getInterval(Interval(3, MAJOR))
    assertEquals(Pitch(E, NATURAL, 4), res)
  }

  @Test
  fun testGetMajorThirdFromF() {
    val res = Pitch(F, NATURAL, 4).getInterval(Interval(3, MAJOR))
    assertEquals(Pitch(A, NATURAL, 4), res)
  }

  @Test
  fun testGetMajorThirdFromBFlat() {
    val res = Pitch(B, FLAT, 4).getInterval(Interval(3, MAJOR))
    assertEquals(Pitch(D, NATURAL, 5), res)
  }

  @Test
  fun testGetAugmentedThird() {
    val res = default.getInterval(Interval(3, AUGMENTED))
    assertEquals(Pitch(E, SHARP, 4), res)
  }

  @Test
  fun testGetPerfectFourth() {
    val res = default.getInterval(Interval(4, PERFECT))
    assertEquals(Pitch(F, NATURAL, 4), res)
  }

  @Test
  fun testGetAugmentedFourth() {
    val res = default.getInterval(Interval(4, AUGMENTED))
    assertEquals(Pitch(F, SHARP, 4), res)
  }


  @Test
  fun testGetDiminishedFourth() {
    val res = default.getInterval(Interval(4, DIMINISHED))
    assertEquals(Pitch(F, FLAT, 4), res)
  }

  @Test
  fun testGetDiminishedFourthFromG() {
    val res = Pitch(G, NATURAL, 4).getInterval(Interval(4, DIMINISHED))
    assertEquals(Pitch(C, FLAT, 4), res)
  }

  @Test
  fun testGetPerfectFifth() {
    val res = default.getInterval(Interval(5,  PERFECT))
    assertEquals(Pitch(G, NATURAL, 4), res)
  }

  @Test
  fun testGetPerfectFifthAcrossOctave() {
    val res = Pitch(A, NATURAL, 4).getInterval(Interval(5,  PERFECT))
    assertEquals(Pitch(E, NATURAL, 5), res)
  }

  @Test
  fun testGetDiminishedFifth() {
    val res = default.getInterval(Interval(5,  DIMINISHED))
    assertEquals(Pitch(G, FLAT, 4), res)
  }

  @Test
  fun testGetDiminishedFifthAcrossOctave() {
    val res = Pitch(G, NATURAL, 4).getInterval(Interval(5,  DIMINISHED))
    assertEquals(Pitch(D, FLAT, 5), res)
  }

  @Test
  fun testGetAugmentedFifth() {
    val res = default.getInterval(Interval(5,  AUGMENTED))
    assertEquals(Pitch(G, SHARP, 4), res)
  }

  @Test
  fun testGetAugmentedFifthAcrossOctave() {
    val res = Pitch(F, NATURAL, 4).getInterval(Interval(5,  AUGMENTED))
    assertEquals(Pitch(C, SHARP, 5), res)
  }

  @Test
  fun testGetAugmentedFifthAwkward() {
    val res = Pitch(B, FLAT, 4).getInterval(Interval(5,  AUGMENTED))
    assertEquals(Pitch(F, SHARP, 5), res)
  }

  @Test
  fun testGetMinorSixth() {
    val res = default.getInterval(Interval(6,  MINOR))
    assertEquals(Pitch(A, FLAT, 4), res)
  }

  @Test
  fun testGetMajorSixth() {
    val res = default.getInterval(Interval(6,  MAJOR))
    assertEquals(Pitch(A, NATURAL, 4), res)
  }

  @Test
  fun testGetAugmentedSixth() {
    val res = default.getInterval(Interval(6,  AUGMENTED))
    assertEquals(Pitch(A, SHARP, 4), res)
  }

  @Test
  fun testGetMinorSeventh() {
    val res = default.getInterval(Interval(7,  MINOR))
    assertEquals(Pitch(B, FLAT, 4), res)
  }

  @Test
  fun testGetMinorSeventhFromA() {
    val res = Pitch(A, NATURAL, 4).getInterval(Interval(7, MINOR))
    assertEquals(Pitch(G, NATURAL, 5), res)
  }

  @Test
  fun testGetMinorSeventhAcrossOctave() {
    val res = Pitch(A, FLAT, 4).getInterval(Interval(7,  MINOR))
    assertEquals(Pitch(G, FLAT, 5), res)
  }

  @Test
  fun testGetMajorSeventh() {
    val res = default.getInterval(Interval(7,  MAJOR))
    assertEquals(Pitch(B, NATURAL, 4), res)
  }

  @Test
  fun testGetMajorSeventhFromA() {
    val res = Pitch(A, NATURAL, 4).getInterval(Interval(7, MAJOR))
    assertEquals(Pitch(G, SHARP, 5), res)
  }

  @Test
  fun testGetOctave() {
    val res = default.getInterval(Interval(8,  PERFECT))
    assertEquals(Pitch(C, NATURAL, 5), res)
  }

  @Test
  fun testGetMinorSecondSharpNote() {
    val res = defaultSharp.getInterval(Interval(2, MINOR))
    assertEquals(Pitch(D, NATURAL, 4), res)
  }

  @Test
  fun testGetMajorSecondSharpNote() {
    val res = defaultSharp.getInterval(Interval(2, MAJOR))
    assertEquals(Pitch(D, SHARP, 4), res)
  }

  @Test
  fun testGetAugmentedSecondSharpNote() {
    val res = defaultSharp.getInterval(Interval(2, AUGMENTED))
    assertEquals(Pitch(D, DOUBLE_SHARP, 4), res)
  }

  @Test
  fun testGetMinorThirdSharpNote() {
    val res = defaultSharp.getInterval(Interval(3, MINOR))
    assertEquals(Pitch(E, NATURAL, 4), res)
  }

  @Test
  fun testGetMinorThirdSharpNoteSecondIsAwkward() {
    val res = Pitch(D, SHARP, 4).getInterval(Interval(3, MINOR))
    assertEquals(Pitch(F, SHARP, 4), res)
  }

  @Test
  fun testGetMajorThirdSharpNote() {
    val res = defaultSharp.getInterval(Interval(3, MAJOR))
    assertEquals(Pitch(E, SHARP, 4), res)
  }

  @Test
  fun testGetPerfectFourthSharpNote() {
    val res = defaultSharp.getInterval(Interval(4, PERFECT))
    assertEquals(Pitch(F, SHARP, 4), res)
  }

  @Test
  fun testGetAugmentedFourthSharpNote() {
    val res = defaultSharp.getInterval(Interval(4, AUGMENTED))
    assertEquals(Pitch(F, DOUBLE_SHARP, 4), res)
  }

  @Test
  fun testGetPerfectFifthSharpNote() {
    val res = defaultSharp.getInterval(Interval(5, PERFECT))
    assertEquals(Pitch(G, SHARP, 4), res)
  }

  @Test
  fun testGetDiminishedFifthSharpNote() {
    val res = defaultSharp.getInterval(Interval(5, DIMINISHED))
    assertEquals(Pitch(G, NATURAL, 4), res)
  }

  @Test
  fun testGetMajorSeventhSharpNote() {
    val res = defaultSharp.getInterval(Interval(7, MAJOR))
    assertEquals(Pitch(B, SHARP, 5), res)
  }

  @Test
  fun testGetMajorSeventhFromASharp() {
    val res = Pitch(A, SHARP, 4).getInterval(Interval(7, MAJOR))
    assertEquals(Pitch(G, DOUBLE_SHARP, 5), res)
  }

  @Test
  fun testGetMinorSeventhSharpNote() {
    val res = defaultSharp.getInterval(Interval(7, MINOR))
    assertEquals(Pitch(B, NATURAL, 4), res)
  }

  @Test
  fun testGetMinorSecondFlatNote() {
    val res = defaultFlat.getInterval(Interval(2, MINOR))
    assertEquals(Pitch(E, DOUBLE_FLAT, 4), res)
  }

  @Test
  fun testGetMajorSecondFlatNote() {
    val res = defaultFlat.getInterval(Interval(2, MAJOR))
    assertEquals(Pitch(E, FLAT, 4), res)
  }

  @Test
  fun testGetMinorThirdFlatNote() {
    val res = defaultFlat.getInterval(Interval(3, MINOR))
    assertEquals(Pitch(F, FLAT, 4), res)
  }

  @Test
  fun testGetAugmentedSecondFlatNote() {
    val res = defaultFlat.getInterval(Interval(2, AUGMENTED))
    assertEquals(Pitch(E, NATURAL, 4), res)
  }

  @Test
  fun testGetMinorSecondAwkwardNote() {
    val res = defaultAwkward.getInterval(Interval(2, MINOR))
    assertEquals(Pitch(C, NATURAL, 5), res)
  }

  @Test
  fun testGetPerfectFourthFlatNote() {
    val res = defaultFlat.getInterval(Interval(4, PERFECT))
    assertEquals(Pitch(G, FLAT, 4), res)
  }

  @Test
  fun testGetPerfectFourthAwkwardFlatNote() {
    val res = Pitch(B, FLAT, 4).getInterval(Interval(4, PERFECT))
    assertEquals(Pitch(E, FLAT, 5), res)
  }

  @Test
  fun testGetPerfectFourthSecondIsAwkward() {
    val res = Pitch(F, NATURAL, 4).getInterval(Interval(4, PERFECT))
    assertEquals(Pitch(B, FLAT, 4), res)
  }

  @Test
  fun testGetAugmentedFourthSecondIsAwkward() {
    val res = Pitch(F, NATURAL, 4).getInterval(Interval(4, AUGMENTED))
    assertEquals(Pitch(B, NATURAL, 4), res)
  }

  @Test
  fun testGetAugmentedFourthFlatNote() {
    val res = defaultFlat.getInterval(Interval(4, AUGMENTED))
    assertEquals(Pitch(G, NATURAL, 4), res)
  }

  @Test
  fun testGetAugmentedFourthEFlat() {
    val res = Pitch(E, FLAT, 4).getInterval(Interval(4, AUGMENTED))
    assertEquals(Pitch(A, NATURAL, 4), res)
  }

  @Test
  fun testGetAugmentedFourthAwkwardFlatNote() {
    val res = Pitch(B, FLAT, 4).getInterval(Interval(4, AUGMENTED))
    assertEquals(Pitch(E, NATURAL, 5), res)
  }

  @Test
  fun testGetMajorSecondAwkwardNote() {
    val res = defaultAwkward.getInterval(Interval(2, MAJOR))
    assertEquals(Pitch(C, SHARP, 5), res)
  }

  @Test
  fun testGetPerfectFourthAwkwardNote() {
    val res = Pitch(B, NATURAL, 4).getInterval(Interval(4, PERFECT))
    assertEquals(Pitch(E, NATURAL, 5), res)
  }

  @Test
  fun testGetDiminishedFourthAwkwardNote() {
    val res = Pitch(B, NATURAL, 4).getInterval(Interval(4, DIMINISHED))
    assertEquals(Pitch(E, FLAT, 5), res)
  }

  @Test
  fun testGetAugmentedFourthAwkwardNote() {
    val res = Pitch(B, NATURAL, 4).getInterval(Interval(4, AUGMENTED))
    assertEquals(Pitch(E, SHARP, 5), res)
  }

  @Test
  fun testGetAugmentedFifthAwkwardNote() {
    val res = Pitch(B, NATURAL, 4).getInterval(Interval(5, AUGMENTED))
    assertEquals(Pitch(F, DOUBLE_SHARP, 5), res)
  }

  @Test
  fun testGetAugmentedFourthFromE() {
    val res = Pitch(E, NATURAL, 4).getInterval(Interval(4, AUGMENTED))
    assertEquals(Pitch(A, SHARP, 4), res)
  }

  @Test
  fun testGetPerfectFifthAwkwardNote() {
    val res = Pitch(B, NATURAL, 4).getInterval(Interval(5, PERFECT))
    assertEquals(Pitch(F, SHARP, 5), res)
  }

  @Test
  fun testGetMinorSixthAwkwardNote() {
    val res = defaultAwkward.getInterval(Interval(6, MINOR))
    assertEquals(Pitch(G, NATURAL, 5), res)
  }

  @Test
  fun testGetMajorThirdSecondAwkwardNote() {
    val res = Pitch(A, SHARP, 4).getInterval(Interval(3, MAJOR))
    assertEquals(Pitch(C, DOUBLE_SHARP, 5), res)
  }

  @Test
  fun testGetAugmentedSecondAwkwardNote() {
    val res = defaultAwkward.getInterval(Interval(2, AUGMENTED))
    assertEquals(Pitch(C, DOUBLE_SHARP, 5), res)
  }

  @Test
  fun testGetPerfectFourthSecondAwkwardNote() {
    val res = Pitch(G, FLAT, 4).getInterval(Interval(4, PERFECT))
    assertEquals(Pitch(C, FLAT, 4), res)
  }

  @Test
  fun testGetAugmentedFourthSecondAwkwardNote() {
    val res = Pitch(F, SHARP, 4).getInterval(Interval(4, AUGMENTED))
    assertEquals(Pitch(B, SHARP, 5), res)
  }


  @Test
  fun testGetMajorSixthHigherSecondAwkwardNote() {
    val res = Pitch(A, NATURAL, 4).getInterval(Interval(6, MAJOR))
    assertEquals(Pitch(F, SHARP, 5), res)
  }

  @Test
  fun testGetPerfectFourthSharpNoteSecondAwkwardNote() {
    val res = Pitch(F, SHARP, 4).getInterval(Interval(4, PERFECT))
    assertEquals(Pitch(B, NATURAL, 4), res)
  }

  @Test
  fun testGetDiminishedFourthSharpNoteSecondAwkwardNote() {
    val res = Pitch(F, SHARP, 4).getInterval(Interval(4, DIMINISHED))
    assertEquals(Pitch(B, FLAT, 4), res)
  }

  private val default = Pitch(C, NATURAL, 4)
  private val defaultSharp = Pitch(C, SHARP, 4)
  private val defaultFlat = Pitch(D, FLAT, 4)
  private val defaultAwkward = Pitch(B, NATURAL, 4)
}