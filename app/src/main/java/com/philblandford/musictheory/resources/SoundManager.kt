package com.philblandford.musictheory.resources

import com.philblandford.kscore.api.Instrument
import com.philblandford.kscore.api.InstrumentGroup
import com.philblandford.kscore.api.SoundManager

object MTSoundManager : SoundManager {
  override fun assignInstrument(instrumentName: String, group: String) {
    TODO("Not yet implemented")
  }

  override fun clearUser() {
    TODO("Not yet implemented")
  }

  override fun close() {
    TODO("Not yet implemented")
  }

  override fun createGroup(name: String, instruments: List<Instrument>) {
    TODO("Not yet implemented")
  }

  override fun getInstrument(name: String): Instrument? {
    TODO("Not yet implemented")
  }

  override fun getInstrumentGroup(name: String): InstrumentGroup? {
    TODO("Not yet implemented")
  }

  override fun getInstrumentGroups(): List<InstrumentGroup> {
    TODO("Not yet implemented")
  }

  override fun noteOff(midiVal: Int, channel: Int) {
    TODO("Not yet implemented")
  }

  override fun noteOn(midiVal: Int, velocity: Int, channel: Int) {
    TODO("Not yet implemented")
  }

  override fun pedalEvent(on: Boolean, channel: Int) {
    TODO("Not yet implemented")
  }

  override fun programChange(program: Int, channel: Int, soundFont: String, bank: Int) {
    TODO("Not yet implemented")
  }

  override fun refresh() {
    TODO("Not yet implemented")
  }

  override fun reset() {
    TODO("Not yet implemented")
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
    TODO("Not yet implemented")
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
    TODO("Not yet implemented")
  }

  override fun stopAllNotes(soundFont: String?) {
    TODO("Not yet implemented")
  }
}