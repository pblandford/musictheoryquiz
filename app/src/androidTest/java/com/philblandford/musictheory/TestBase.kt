package com.philblandford.musictheory

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.philblandford.kscore.api.DrawableGetter
import com.philblandford.kscore.api.KScore
import com.philblandford.kscore.api.KScoreImpl
import com.philblandford.kscore.api.SoundManager
import com.philblandford.kscore.engine.core.area.KDrawable
import com.philblandford.kscore.engine.core.area.factory.DrawableArgs
import com.philblandford.musictheory.resources.AndroidStringResolver
import com.philblandford.musictheory.resources.DefaultRandom
import com.philblandford.musictheory.resources.RandomNumber
import com.philblandford.musictheory.resources.StringResolver
import org.junit.Before
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
open class TestBase {

  private val context = InstrumentationRegistry.getInstrumentation().targetContext

  @Before
  open fun setup() {
    initKoin()
  }

  protected open fun getRandom(): RandomNumber {
    return DefaultRandom
  }


  private fun initKoin() {
    stopKoin()
    val module = module {
      single<DrawableGetter> { TestDrawableGetter }
      single<SoundManager> { TestSoundManager }
      single<StringResolver> { AndroidStringResolver(context) }
      single { getRandom() }
    }
    startKoin { modules(module) }
  }
}
