package com.philblandford.musictheory

import ResourceManager
import SamplerManager
import TextFontManager
import android.app.Application
import com.philblandford.androidimpl.drawingcompose.ComposeDrawableGetter
import com.philblandford.androidimpl.resource.AndroidResourceManager
import com.philblandford.androidimpl.sound.AndroidSoundManagerFluid
import com.philblandford.androidimpl.sound.DefaultInstrumentGetter
import com.philblandford.androidimpl.sound.FluidSamplerManager
import com.philblandford.androidimpl.text.AndroidTextFontManager
import com.philblandford.kscore.api.*
import com.philblandford.musictheory.resources.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.dsl.module

class BaseApplication : Application() {
  override fun onCreate() {
    super.onCreate()
    initKoin()
  }

  private fun initKoin() {
    val module = module {
      single<DrawableGetter> { ComposeDrawableGetter(androidContext(), get()) }
      single<TextFontManager> { AndroidTextFontManager(androidContext()) }
      single<InstrumentGetter> { DefaultInstrumentGetter(get(), get()).apply { refresh() } }
      single<ResourceManager> { AndroidResourceManager(androidContext(), get()) }
      single<PreferenceGetter> { AndroidPreferenceGetter(androidContext()) }
      single<AssetManager> { AndroidAssetManager(androidContext()) }
      single<SamplerManager> { FluidSamplerManager(get()) }
      single<SoundManager> { AndroidSoundManagerFluid(get(), get()) }
      single<StringResolver> { AndroidStringResolver(androidContext()) }
      single<RandomNumber> { DefaultRandom }
      single<Repository> { StubRepository() }
      single<KScore> { KScoreImpl(Logger, get(), get()) }
      single<SoundPlayer> { AndroidSoundPlayer(androidContext()) }
    }
    startKoin { modules(module).androidContext(applicationContext) }
  }

}