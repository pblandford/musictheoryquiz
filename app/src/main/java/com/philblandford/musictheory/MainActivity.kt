package com.philblandford.musictheory

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.Dimension
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.WithConstraints
import androidx.compose.ui.platform.setContent
import androidx.compose.ui.res.integerArrayResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.*
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import com.philblandford.musictheory.resources.Repository
import com.philblandford.musictheory.ui.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import kotlin.random.Random


class MainActivity : AppCompatActivity(), KoinComponent {

  private lateinit var reviewManager: ReviewManager
  private lateinit var interstitialAd: InterstitialAd

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    reviewManager = ReviewManagerFactory.create(this)
    initInterstitial()

    val repository: Repository by inject()

    MobileAds.initialize(this) { }

    setContent {

      WithConstraints(Modifier.fillMaxSize()) {
        val block = block()
        MusicTheoryTheme {

          Surface(color = MaterialTheme.colors.background) {
            ConstraintLayout(Modifier.fillMaxSize()) {
              val (main, ads) = createRefs()
              MainView(repository, Modifier.constrainAs(main) {
                top.linkTo(parent.top)
                bottom.linkTo(ads.top)
                height = Dimension.fillToConstraints
              })
              AdBanner(Modifier.constrainAs(ads) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
              })
            }
          }
        }
      }
    }
  }

  @Composable
  private fun MainView(repository: Repository, modifier: Modifier) {
    val navController = rememberNavController()

    Box(modifier) {
      NavHost(navController, startDestination = "choose") {
        composable("choose") {
          repository.clear()
          ChooseQuiz() {
            navController.navigate("question")
          }
        }
        composable("question") {
          QuestionView {
            navController.navigate("results")
          }
        }
        composable("results") {
          Results() {
            adOrRating()
            navController.navigate("choose")
          }
        }
      }
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
    AndroidView(viewBlock = { adview }, modifier = modifier) {
      val adRequest = AdRequest.Builder().build()
      it.loadAd(adRequest)
    }
  }

  private fun initInterstitial() {
    interstitialAd = InterstitialAd(this).apply {
      val res =
        if (BuildConfig.DEBUG) R.string.interstitial_id_test else R.string.interstitial_id_release
      adUnitId = getString(res)
      loadAd(AdRequest.Builder().build())
    }
  }

  private fun showInterstitial() {
    if (interstitialAd.isLoaded) {
      interstitialAd.show()
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
