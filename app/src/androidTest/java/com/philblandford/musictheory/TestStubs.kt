package com.philblandford.musictheory

import com.philblandford.kscore.api.DrawableGetter
import com.philblandford.kscore.api.Instrument
import com.philblandford.kscore.api.InstrumentGroup
import com.philblandford.kscore.api.SoundManager
import com.philblandford.kscore.engine.core.area.KDrawable
import com.philblandford.kscore.engine.core.area.factory.DrawableArgs
import com.philblandford.kscore.engine.types.ClefType


private class TestDrawable : KDrawable {
  override val effectiveHeight: Int
    get() = 0
  override val export: Boolean
    get() = false
  override val height: Int
    get() = 0
  override val trim: Int
    get() = 0
  override val width: Int
    get() = 0

  override fun draw(x: Int, y: Int, export: Boolean, vararg args: Any) {
  }
}

object TestDrawableGetter : DrawableGetter {
  override fun getDrawable(drawableArgs: DrawableArgs): KDrawable? {
    return TestDrawable()
  }

  override fun prepare(vararg args: Any) {
  }
}

object TestSoundManager : SoundManager {

  private val instrumentsGroups = listOf(
    InstrumentGroup(
      "Strings", listOf(
        Instrument(
          "Violin", "Vln", "Strings", 42, 0, listOf(ClefType.TREBLE),
          "default", 0
        )
      )
    )
  )

  override fun assignInstrument(instrumentName: String, group: String) {
  }

  override fun clearUser() {
  }

  override fun close() {
  }

  override fun createGroup(name: String, instruments: List<Instrument>) {
  }

  override fun getInstrument(name: String): Instrument? {
    return instrumentsGroups.flatMap { it.instruments }.find { it.name == name }
  }

  override fun getInstrumentGroup(name: String): InstrumentGroup? {
    return instrumentsGroups.find { it.name == name }
  }

  override fun getInstrumentGroups(): List<InstrumentGroup> {
    return instrumentsGroups
  }

  override fun noteOff(midiVal: Int, channel: Int) {
  }

  override fun noteOn(midiVal: Int, velocity: Int, channel: Int) {
  }

  override fun pedalEvent(on: Boolean, channel: Int) {
  }

  override fun programChange(program: Int, channel: Int, soundFont: String, bank: Int) {
  }

  override fun refresh() {
  }

  override fun reset() {
  }

  override fun soundSingleNote(
    midiVal: Int,
    program: Int,
    velocity: Int,
    length: Int,
    percussion: Boolean,
    soundFont: String,
    bank: Int,
    channel: Int
  ) {
  }

  override fun soundSingleNoteNoStop(
    midiVal: Int,
    program: Int,
    velocity: Int,
    percussion: Boolean,
    soundFont: String,
    bank: Int,
    channel: Int
  ) {
  }

  override fun stopAllNotes(soundFont: String?) {
  }
}