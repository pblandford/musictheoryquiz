package com.philblandford.musictheory.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.philblandford.musictheory.engine.Question
import com.philblandford.musictheory.resources.PreferenceGetter
import com.philblandford.musictheory.resources.Repository
import com.philblandford.musictheory.resources.SoundPlayer
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

abstract class BaseViewModel<T> : ViewModel(), KoinComponent {

  private val model = MutableLiveData<T>()
  private val intents = Channel<UIIntent>(Channel.UNLIMITED)
  protected val repository: Repository by inject()
  protected val preferenceGetter:PreferenceGetter by inject()
  private val soundPlayer: SoundPlayer by inject()

  fun init() {
    initModel()?.let {
      model.value = it
      mainLoop()
    }
  }

  protected open fun initModel():T? = null

  fun getModel(): LiveData<T> = model

  fun receiveIntent(uiIntent: UIIntent) {
    viewModelScope.launch {
      intents.send(uiIntent)
    }
  }

  @OptIn(InternalCoroutinesApi::class)
  private fun mainLoop() {
    viewModelScope.launch {
      intents.consumeAsFlow().collect(object : FlowCollector<UIIntent> {
        override suspend fun emit(value: UIIntent) {
          model.value?.let { m ->
            val newModel = doHandleIntent(m, value)
            doUpdate(newModel)
          }
        }
      })
    }
  }

  protected open fun doHandleIntent(model:T, intent: UIIntent):T = model

  private fun doUpdate(model: T) {
    this.model.postValue(model)
  }

}
