package uk.ac.aber.dcs.cs31620.multilingualapp.ui.vocabPage


import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.multilingualapp.R
import uk.ac.aber.dcs.cs31620.multilingualapp.model.LanguageValue
import uk.ac.aber.dcs.cs31620.multilingualapp.model.LanguageValueViewModel
import uk.ac.aber.dcs.cs31620.multilingualapp.model.Word
import uk.ac.aber.dcs.cs31620.multilingualapp.model.WordViewModel
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.components.WordCard

/**
 * This function represents the top level used to connect the UI with the view model in order to be
 * able to allow communication with the database. It is also the function to be used to represent
 * the screen in the MainActivity navigation graph.
 *
 * @param navController is used to control the screen's state through the overall navigation graph.
 * @param wordViewModel is used to connect the WordViewModel class to the UI from this screen
 * @param languageValueViewModel is used to connect the LanguageValueViewModel class to the UI from this
 * screen
 */
@Composable
fun VocabListScreenTopLevel(
    navController: NavHostController,
    wordViewModel: WordViewModel = viewModel(),
    languageValueViewModel: LanguageValueViewModel = viewModel()
) {
    val wordList by wordViewModel.wordsInDatabase.observeAsState(listOf())
    val languagesList by languageValueViewModel.languageInDatabase.observeAsState(listOf())

    VocabListScreen(
        wordsList = wordList,
        navController = navController,
        languagesList = languagesList,
        deleteWord = {wordToDelete ->
            wordViewModel.deleteWord(wordToDelete)
        },
        /*updateWord = {wordToUpdate ->
            wordViewModel.updateWord(wordToUpdate)
        }*/
    )
}

/**
 * This function creates the UI and the actions for the vocabulary list. This list is formed by the
 * words from the Word database. The current languages present in the database are shown on top of
 * the page.
 *
 * @param navController used to control the navigation state of the screen through the actions
 * carried out by the app.
 * @param wordsList This is the list of Word objects which the view model uses to extract the words
 * from the database.
 * @param languagesList This is the list of LanguageValue objects which the view model uses to
 * extract the languages from the database.
 * @param deleteWord this carries out the deletion of the selected word in the words database.
 */
@Composable
fun VocabListScreen(
    navController: NavHostController,
    wordsList: List<Word> = listOf(),
    languagesList: List<LanguageValue>,
    deleteWord: (Word) -> Unit = {},
    //updateWord: (Word) -> Unit = {}
) {
    if (languagesList.isNotEmpty()) {
        val coroutineScope = rememberCoroutineScope()

        TopLevelScaffold(
            navController = navController,
            coroutineScope = coroutineScope,
            pageContent = { innerPadding ->
                Surface(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                ) {
                    val optionsList = stringArrayResource(id = R.array.button_arrangement).toList()
                    //var selectedOption by rememberSaveable { mutableStateOf(optionsList[0]) }
                    val nativeL = languagesList[languagesList.lastIndex].nativeLanguage
                    val foreignL = languagesList[languagesList.lastIndex].foreignLanguage
                    val context = LocalContext.current

                    Row(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp)
                    ) {

                        Text(
                            text = nativeL,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 15.dp, end = 8.dp, bottom = 10.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )

                        Text(
                            text = foreignL,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 40.dp, end = 2.dp, bottom = 10.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )
                    }

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(1),
                        modifier = Modifier
                            .padding(start = 10.dp, bottom = 4.dp, top = 15.dp, end = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        contentPadding = PaddingValues(top = 30.dp),
                    ) {
                        val sortedList = wordsList.sortedWith(compareBy { it.nativeWord })
                        items(sortedList) {
                            WordCard(
                                word = it,
                                modifier = Modifier.padding(end = 4.dp, top = 4.dp),
                                deleteAction = { word ->
                                    modifyWord(
                                        nativeWord = word.nativeWord,
                                        foreignWord = word.foreignWord,
                                        doAction = {
                                            deleteWord(word)
                                            Toast.makeText(context, "The words ${word.nativeWord} and ${word.foreignWord} have been deleted.", Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                }
                            )
                        }
                    }
                }
            }
        )
    }
}

/**
 * This function is used to implement the viewModel functions within the UI
 *
 * @param nativeWord the native word to perform the action with.
 * @param foreignWord the foreign word to perform the action with.
 * @param doAction the action which uses the native and foreign word to get carried out.
 */
private fun modifyWord(
    nativeWord: String,
    foreignWord: String,
    doAction: (Word) -> Unit = {}
){
    if(nativeWord.isNotEmpty() && foreignWord.isNotEmpty()){
        val word = Word(
            id = 0,
            nativeWord = nativeWord,
            foreignWord = foreignWord
        )
        doAction(word)
    }

}