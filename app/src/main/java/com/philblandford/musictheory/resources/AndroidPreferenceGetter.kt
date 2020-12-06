package com.philblandford.musictheory.resources

import android.content.Context
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import com.philblandford.kscore.engine.types.ClefType

class AndroidPreferenceGetter(private val context: Context) : PreferenceGetter {

  private val clefKey = "CLEFS"

  override fun setPreferredClefs(clefs: List<ClefType>) {
    val string = clefs.joinToString(",")
    PreferenceManager.getDefaultSharedPreferences(context).edit().putString(clefKey, string).apply()
  }

  override fun getPreferredClefs(): List<ClefType> {
    val string = PreferenceManager.getDefaultSharedPreferences(context).getString(clefKey, "TREBLE")
    return string?.split(",")?.map { ClefType.valueOf(it) } ?: listOf(ClefType.TREBLE)
  }

  override fun allClefs(): List<ClefType> {
    return listOf(ClefType.TREBLE, ClefType.BASS, ClefType.ALTO, ClefType.TENOR)
  }
}