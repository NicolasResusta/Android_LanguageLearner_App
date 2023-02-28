package uk.ac.aber.dcs.cs31620.multilingualapp.ui.navigation

/**
 * This class holds the different screens which would be shown throughout the app based on the route
 * given to them.
 *
 * @param route this helps the navController from the navigation graph to follow the state of the
 * different screens through the flow of actions in the app's lifecycle.
 */
sealed class Screen(
    val route: String
){
    object OpeningScreen : Screen("o_screen")
    object Home : Screen("home")
    object VocabList : Screen("vocabList")
    object CorrectWordGameScreen : Screen("correctWordGame")
    object HangmanGameScreen : Screen("hangmanGame")
    object AnagramGameScreen : Screen("anagramGame")
    object LanguageChangeScreen : Screen("languageChange")
}

val screens = listOf(
    Screen.Home,
    Screen.VocabList
)
