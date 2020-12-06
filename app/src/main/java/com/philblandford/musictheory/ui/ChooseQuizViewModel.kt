package com.philblandford.musictheory.ui

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import com.philblandford.kscore.engine.core.representation.Standalone
import com.philblandford.kscore.engine.types.ClefType
import com.philblandford.musictheory.R
import com.philblandford.musictheory.resources.QuizDescriptor
import com.philblandford.musictheory.resources.QuizType


data class QuizDescriptorDisplay(
  val quizDescriptor: QuizDescriptor, val resId: Int,
  val color: Color
)

data class ClefDescriptor(val clefType: ClefType, val enabled:Boolean, val resId: Int)

data class ChooseQuizModel(val clefs:List<ClefDescriptor>, val quizzes:List<QuizDescriptorDisplay>)

class ChooseQuizViewModel : BaseViewModel<ChooseQuizModel>() {

  private val resIds = mapOf(
    QuizType.KEY to R.drawable.key_signature,
    QuizType.NOTE to R.drawable.note,
    QuizType.TERMS to R.drawable.term,
    QuizType.TRIAD to R.drawable.triad,
    QuizType.INTERVAL to R.drawable.interval,
    QuizType.SCALE to R.drawable.scale
  )

  private val colors = mapOf(
    QuizType.KEY to yellow,
    QuizType.NOTE to lightGreen,
    QuizType.TERMS to cyan,
    QuizType.TRIAD to red,
    QuizType.INTERVAL to purple,
    QuizType.SCALE to orange
  )

  init {
    init()
  }

  private fun getQuizzes() = repository.getQuizzes().mapNotNull { qd ->
    resIds[qd.type]?.let { resId ->
      colors[qd.type]?.let { color ->
        QuizDescriptorDisplay(qd, resId, color)
      }
    }
  }

  override fun initModel(): ChooseQuizModel {
    return ChooseQuizModel(getClefs(), getQuizzes())
  }

  override fun doHandleIntent(model: ChooseQuizModel, intent: UIIntent): ChooseQuizModel {
    return when (intent) {
      is UIIntent.SelectClef -> {
        setClef(intent.clefType, intent.yes)
        initModel()
      }
      else -> model
    }
  }

  private fun setClef(clefType: ClefType, yes:Boolean) {
    var existing = preferenceGetter.getPreferredClefs()
    if (yes) {
      existing = (existing + clefType).distinct()
    } else {
      if (existing != listOf(clefType)) {
        existing = existing - clefType
      }
    }
    preferenceGetter.setPreferredClefs(existing)
  }

  private fun getClefs():List<ClefDescriptor> {
    val all = preferenceGetter.allClefs()
    val selected = preferenceGetter.getPreferredClefs()
    return all.mapNotNull { type ->
      type.getResId()?.let { resId ->
        ClefDescriptor(type, selected.contains(type), resId)
      }
    }
  }

  private fun ClefType.getResId():Int? {
   return when (this) {
     ClefType.TREBLE -> R.drawable.treble_clef
     ClefType.BASS -> R.drawable.bass_clef
     ClefType.ALTO -> R.drawable.alto_clef
     ClefType.TENOR -> R.drawable.tenor_clef
     else -> null
   }
  }

}
