package com.philblandford.musictheory

import ResourceManager

import TextFontManager
import android.app.Application
import com.philblandford.kscoreandroid.drawingcompose.ComposeDrawableGetter
import com.philblandford.kscoreandroid.resource.AndroidResourceManager

import com.philblandford.kscore.api.*
import com.philblandford.kscoreandroid.text.AndroidTextFontManager
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
   //   single<InstrumentGetter> { DefaultInstrumentGetter(get(), get()).apply { refresh() } }
      single<ResourceManager> { AndroidResourceManager(androidContext(), get()) }
      single<PreferenceGetter> { AndroidPreferenceGetter(androidContext()) }
      single<AssetManager> { AndroidAssetManager(androidContext()) }
    //  single<SamplerManager> { FluidSamplerManager(get()) }
    //  single<SoundManager> { AndroidSoundManagerFluid(get(), get()) }
      single<StringResolver> { AndroidStringResolver(androidContext()) }
      single<RandomNumber> { DefaultRandom }
      single<Repository> { RepositoryImpl() }
      single<KScore> { KScoreImpl(Logger, get(), get()) }
      single<SoundPlayer> { AndroidSoundPlayer(androidContext()) }
    }
    startKoin { modules(module).androidContext(applicationContext) }
  }

}