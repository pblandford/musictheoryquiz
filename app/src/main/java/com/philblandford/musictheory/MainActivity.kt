package com.philblandford.musictheory

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Text
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.WithConstraints
import androidx.compose.ui.layout.WithConstraintsScope
import androidx.compose.ui.platform.setContent
import androidx.navigation.compose.navigate
import androidx.compose.ui.viewinterop.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.philblandford.kscore.api.KScore
import com.philblandford.musictheory.resources.Repository
import com.philblandford.musictheory.ui.*
import org.koin.core.KoinComponent
import org.koin.core.inject

class MainActivity : AppCompatActivity(), KoinComponent {

  private val kScore: KScore by inject()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val repository:Repository by inject()

    setContent {

      val navController = rememberNavController()

      WithConstraints(Modifier.fillMaxSize()) {
        MusicTheoryTheme {
          Surface(color = MaterialTheme.colors.background) {
            NavHost(navController, startDestination = "choose") {
              composable("choose") {
                ChooseQuiz() {
                  repository.setQuiz(it)
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
                  navController.navigate("choose")
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
fun Greeting(name: String) {
  Text(text = "Hello $name!")
}
