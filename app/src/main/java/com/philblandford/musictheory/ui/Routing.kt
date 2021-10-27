package com.philblandford.musictheory.ui

import androidx.compose.runtime.Composable
import com.github.zsoltk.compose.router.Router

private sealed class Routing {
  object Choose : Routing()
  object Question : Routing()
  object Results : Routing()
}

@Composable
fun MainRoute() {
  Router("Main", Routing.Choose as Routing) { backStack ->
    when (val routing = backStack.last()) {
      Routing.Choose -> {
        ChooseQuiz {
          backStack.push(Routing.Question)
        }
      }
      Routing.Question -> {
        QuestionView {
          backStack.push(Routing.Results)
        }
      }
      Routing.Results -> {
        Results {
          backStack.push(Routing.Choose)
        }
      }
    }
  }
}
