package uk.ac.aber.dcs.cs31620.multilingualapp

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.multilingualapp.datasource.LanguageValueRepository
import uk.ac.aber.dcs.cs31620.multilingualapp.datasource.WordRepository
import uk.ac.aber.dcs.cs31620.multilingualapp.model.LanguageValueViewModel
import uk.ac.aber.dcs.cs31620.multilingualapp.model.WordViewModel
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.anagramGame.AnagramGameScreenTopLevel
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.changeLanguage.ChangeLanguageScreenTopLevel
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.correctWordGame.TopLevelCorrectWordGameScreen
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.home.HomeScreenTopLevel
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.openingScreen.OpeningScreenTopLevel
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.theme.MultinlingualAppTheme
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.vocabPage.VocabListScreenTopLevel
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.hangmanGame.TopLevelHangmanGameScreen

/**
 * Starting activity class. Entry point for the app.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MultinlingualAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    BuildNavigationGraph()
                }
            }
        }
    }
}

/**
 * This function builds the navigation graph which the navController uses to determine which screen
 * should be shown depending on the action carried out or the state the app is in.
 *
 * @param wordViewModel is used to connect the WordViewModel class to the UI from this screen
 * @param languageValueViewModel is used to connect the LanguageValueViewModel class to the UI from this
 * screen
 */
@Composable
private fun BuildNavigationGraph(
    wordViewModel: WordViewModel = viewModel(),
    languageValueViewModel: LanguageValueViewModel = viewModel()
) {
    val languageList by languageValueViewModel.languageInDatabase.observeAsState(listOf())
    val navController = rememberNavController()
    var startDestination = remember { Screen.Home.route }
    val context = LocalContext.current.applicationContext

    LaunchedEffect(key1 = 1) {
        val wordRepository = WordRepository(context as Application)
        val languageRepo = LanguageValueRepository(context as Application)
        wordRepository.getWordsSync()
        languageRepo.getAllLanguagesSync()
    }


    if (languageList.isNullOrEmpty()) {
        startDestination = Screen.OpeningScreen.route

        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable(Screen.Home.route) { HomeScreenTopLevel(navController, wordViewModel) }
            composable(Screen.VocabList.route) {
                VocabListScreenTopLevel(
                    navController,
                    wordViewModel,
                    languageValueViewModel
                )
            }
            composable(Screen.CorrectWordGameScreen.route) {
                TopLevelCorrectWordGameScreen(
                    navController,
                    wordViewModel
                )
            }
            composable(Screen.OpeningScreen.route) {
                OpeningScreenTopLevel(
                    navController,
                    languageValueViewModel
                )
            }
            composable(Screen.HangmanGameScreen.route) { TopLevelHangmanGameScreen(navController, wordViewModel) }
            composable(Screen.AnagramGameScreen.route) {
                AnagramGameScreenTopLevel(
                    navController,
                    wordViewModel
                )
            }
            composable(Screen.LanguageChangeScreen.route) {
                ChangeLanguageScreenTopLevel(
                    navController,
                    languageValueViewModel,
                    wordViewModel
                )
            }
        }
    }

    else if (languageList.isNotEmpty()) {
        NavHost(
            navController = navController,
            startDestination = startDestination
        ) {
            composable(Screen.Home.route) { HomeScreenTopLevel(navController, wordViewModel) }
            composable(Screen.VocabList.route) {
                VocabListScreenTopLevel(
                    navController,
                    wordViewModel,
                    languageValueViewModel
                )
            }
            composable(Screen.CorrectWordGameScreen.route) {
                TopLevelCorrectWordGameScreen(
                    navController,
                    wordViewModel
                )
            }
            composable(Screen.OpeningScreen.route) {
                OpeningScreenTopLevel(
                    navController,
                    languageValueViewModel
                )
            }
            composable(Screen.HangmanGameScreen.route) { TopLevelHangmanGameScreen(navController, wordViewModel) }
            composable(Screen.AnagramGameScreen.route) {
                AnagramGameScreenTopLevel(
                    navController,
                    wordViewModel
                )
            }
            composable(Screen.LanguageChangeScreen.route) {
                ChangeLanguageScreenTopLevel(
                    navController,
                    languageValueViewModel,
                    wordViewModel
                )
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MultinlingualAppTheme {
        Greeting("Android")
    }
}