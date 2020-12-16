package com.philblandford.musictheory

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import com.philblandford.kscore.api.DrawableGetter
import com.philblandford.kscore.api.KScore
import com.philblandford.kscore.api.KScoreImpl
import com.philblandford.kscore.api.SoundManager
import com.philblandford.kscore.engine.core.area.KDrawable
import com.philblandford.kscore.engine.core.area.factory.DrawableArgs
import com.philblandford.musictheory.resources.*
import org.junit.Before
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import java.util.*

@RunWith(AndroidJUnit4::class)
open class TestBase {

  protected val numberStack = Stack<Int>()
  private val mockRandom:RandomNumber = mock()
  private val context = InstrumentationRegistry.getInstrumentation().targetContext

  @Before
  open fun setup() {
    initKoin()
    numberStack.clear()
  }

  protected open fun getRandom(): RandomNumber {
    whenever(mockRandom.randomList(any<List<String>>())).thenAnswer {
      it.arguments.first()
    }
    whenever(mockRandom.getInt(any(), any())).thenAnswer {
      numberStack.pop()
    }

    return mockRandom
  }


  private fun initKoin() {
    stopKoin()
    val module = module {
      single<DrawableGetter> { TestDrawableGetter }
      single<SoundManager> { TestSoundManager }
      single<StringResolver> { AndroidStringResolver(context) }
      single<PreferenceGetter> { AndroidPreferenceGetter(context) }
      single { getRandom() }
    }
    startKoin { modules(module) }
  }
}
