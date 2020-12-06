package com.philblandford.musictheory.resources

import android.content.Context
import android.media.MediaPlayer
import android.util.Log
import java.lang.IllegalStateException

class AndroidSoundPlayer(private val context: Context) : SoundPlayer {

  private var mediaPlayer = MediaPlayer()

  override fun playSound(name: String) {
    try {
      if (mediaPlayer.isPlaying) {
        mediaPlayer.stop()
        mediaPlayer.release()
      }
    } catch (e:Exception) {
      Log.e("ASP", "Some bullshit", e)
    }
    mediaPlayer = MediaPlayer()

    val afd = context.assets.openFd("$name.mp3")
    mediaPlayer.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
    afd.close()
    mediaPlayer.prepare()
    mediaPlayer.start()
  }
}