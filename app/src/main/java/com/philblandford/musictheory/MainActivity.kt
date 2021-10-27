package com.philblandford.musictheory

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester.Companion.createRefs
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.github.zsoltk.compose.backpress.BackPressHandler
import com.github.zsoltk.compose.backpress.LocalBackPressHandler
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.philblandford.kscore.api.DrawableGetter
import com.philblandford.musictheory.resources.Repository
import com.philblandford.musictheory.ui.*
import org.koin.core.KoinComponent
import kotlin.random.Random
import com.philblandford.kscoreandroid.drawingcompose.ComposeDrawableGetter
import org.koin.core.inject


val LocalDimensions: ProvidableCompositionLocal<Dimensions> = compositionLocalOf {
  Dimensions(0.dp)
}

class MainActivity : AppCompatActivity(), KoinComponent {

  private lateinit var reviewManager: ReviewManager
  private lateinit var interstitialAd: InterstitialAd
  private val backPressHandler = BackPressHandler()
  private val drawableGetter:DrawableGetter by inject()


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    reviewManager = ReviewManagerFactory.create(this)
    initInterstitial()
    (drawableGetter as? ComposeDrawableGetter)?.color = Color.White

    MobileAds.initialize(this) { }

    setContent {
      CompositionLocalProvider(
        LocalBackPressHandler provides backPressHandler
      ) {
        ThemeBox {

          Surface(color = MaterialTheme.colors.background) {
            Column(Modifier.fillMaxSize()) {
              MainView(
                Modifier
                  .fillMaxWidth()
                  .weight(1f)
              )
              AdBanner(Modifier.align(Alignment.CenterHorizontally))
            }
          }
        }
      }
    }
  }

  @Composable
  private fun MainView(modifier: Modifier) {
    Box(modifier) {
      MainRoute()
    }
  }

  @Composable
  private fun AdBanner(modifier: Modifier) {
    val adview = AdView(this).apply {
      adSize = AdSize.BANNER
      val idRes = if (BuildConfig.DEBUG) R.string.banner_id_test else R.string.banner_id_release
      adUnitId = getString(idRes)
      val adRequest = AdRequest.Builder().build()
      loadAd(adRequest)
    }
    AndroidView(factory = { adview }, modifier = modifier) {
      val adRequest = AdRequest.Builder().build()
      it.loadAd(adRequest)
    }
  }

  private fun initInterstitial() {
    val res =
      if (BuildConfig.DEBUG) R.string.interstitial_id_test else R.string.interstitial_id_release
    val adUnitId = getString(res)

    var adRequest = AdRequest.Builder().build()
    InterstitialAd.load(this, adUnitId, adRequest, object : InterstitialAdLoadCallback() {
      override fun onAdLoaded(ad: InterstitialAd) {
        interstitialAd = ad
      }
    })
  }

  private fun showInterstitial() {
    interstitialAd.show(this)
  }

  override fun onBackPressed() {
    if (!backPressHandler.handle()) {
      super.onBackPressed()
    }
  }

  private fun adOrRating() {
    val num = Random.nextInt(2)
    if (num == 0) {
      showInterstitial()
    } else {
      getRating()
    }
  }

  private fun getRating() {
    val request = reviewManager.requestReviewFlow()
    request.addOnCompleteListener { req ->
      if (req.isSuccessful) {
        val reviewInfo = request.result
        reviewManager.launchReviewFlow(this, reviewInfo)
      }
    }
  }
}
