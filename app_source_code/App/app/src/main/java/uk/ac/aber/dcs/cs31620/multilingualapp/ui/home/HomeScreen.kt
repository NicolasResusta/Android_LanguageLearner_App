package uk.ac.aber.dcs.cs31620.multilingualapp.ui.home

import android.app.Application
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.multilingualapp.R
import uk.ac.aber.dcs.cs31620.multilingualapp.datasource.LanguageValueRepository
import uk.ac.aber.dcs.cs31620.multilingualapp.datasource.WordRepository
import uk.ac.aber.dcs.cs31620.multilingualapp.model.Word
import uk.ac.aber.dcs.cs31620.multilingualapp.model.WordViewModel
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.components.DefaultSnackbar
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.components.TopLevelScaffold
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.theme.MultinlingualAppTheme

/**
 * This function represents the top level used to connect the UI with the view model in order to be
 * able to allow communication with the database. It is also the function to be used to represent
 * the screen in the MainActivity navigation graph.
 *
 * @param navController is used to control the screen's state through the overall navigation graph.
 * @param wordViewModel is used to connect the WordViewModel class to the UI from this screen
 */
@Composable
fun HomeScreenTopLevel(
    navController: NavHostController,
    wordViewModel: WordViewModel = viewModel()
){
    HomeScreen(
        navController = navController,
        insertWord = {newWord ->
            wordViewModel.insertWord(newWord)
        },
        deleteWord = {selectedWord ->
            wordViewModel.deleteWord(selectedWord)
        }
    )
}

/**
 * This function represents the home screen, which is where the user inputs both the native and
 * foreign words they want to submit and upload to the database.
 *
 * @param navController used to control the navigation state of the screen through the actions
 * carried out by the app.
 * @param insertWord this is the lambda function which performs an insertion of a word into the
 * database based on the view model function of it.
 * @param deleteWord this is the lambda function which performs an deletion of a word from the
 * database based on the view model function of it.
 */
@Composable
fun HomeScreen(
    navController: NavHostController,
    insertWord: (Word) -> Unit = {},
    deleteWord: (Word) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var userNativeWord by rememberSaveable { mutableStateOf("") }
    var userForeignWord by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current
    var onDismissState by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current.applicationContext

    TopLevelScaffold(
        navController = navController,
        coroutineScope = coroutineScope,
        snackbarHostState = snackbarHostState,
        pageContent = { innerPadding ->
            Surface(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                focusManager.clearFocus()
                            })
                        }
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                    ) {
                        OutlinedTextField(
                            value = userNativeWord,
                            label = {
                                Text(text = stringResource(R.string.home_page_message1))
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            onValueChange = { languageValue ->
                                userNativeWord = languageValue
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                    ) {
                        OutlinedTextField(
                            value = userForeignWord,
                            label = {
                                Text(text = stringResource(R.string.home_page_message2))
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            onValueChange = { languageValue ->
                                userForeignWord = languageValue
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            singleLine = true
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                if(userForeignWord == "" || userNativeWord == ""){
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "If the input words are empty, they " +
                                                    "won't be added.",
                                            //actionLabel = "Undo"
                                        )
                                    }
                                }
                                else {
                                    modifyWord(
                                        nativeWord = userNativeWord,
                                        foreignWord = userForeignWord,
                                        doAction = { newWord ->
                                            insertWord(newWord)
                                        }
                                    )
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "The words have been added",
                                            //actionLabel = "Undo"
                                        )
                                    }
                                    focusManager.clearFocus()
                                    userNativeWord = ""
                                    userForeignWord = ""
                                    onDismissState = false
                                }
                            },
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .weight(1f),
                        ) {
                            Text(text = stringResource(id = R.string.submit))
                        }

                        Spacer(modifier = Modifier.width(30.dp))

                        Button(
                            onClick = {
                                userNativeWord = ""
                                userForeignWord = ""
                                focusManager.clearFocus()
                            },
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .weight(1f),
                        ) {
                            Text(text = stringResource(id = R.string.clear))
                        }
                    }
                }
            }
        },
        snackbarContent = { data ->
            DefaultSnackbar(
                data = data,
                modifier = Modifier.padding(bottom = 4.dp),
                onDismiss = {
                    if(onDismissState){
                    modifyWord(
                        nativeWord = userNativeWord,
                        foreignWord = userForeignWord,
                        doAction = { wordToDelete ->
                            deleteWord(wordToDelete)
                        }
                    )
                    }
                    data.dismiss()
                }
            )
        },
    )
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

@Preview
@Composable
private fun HomeScreenPreview() {
    val navController = rememberNavController()
    MultinlingualAppTheme(dynamicColor = false) {
        HomeScreen(navController)
    }
}