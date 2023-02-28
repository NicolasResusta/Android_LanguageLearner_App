package uk.ac.aber.dcs.cs31620.multilingualapp.ui.correctWordGame

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.Interaction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusTarget
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.multilingualapp.R
import uk.ac.aber.dcs.cs31620.multilingualapp.model.Word
import uk.ac.aber.dcs.cs31620.multilingualapp.model.WordViewModel
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.components.ScaffoldCorrectWordGameScreen
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.home.HomeScreen
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.navigation.Screen
import kotlin.math.absoluteValue
import kotlin.random.Random

/**
 * This function represents the top level used to connect the UI with the view model in order to be
 * able to allow communication with the database. It is also the function to be used to represent
 * the screen in the MainActivity navigation graph.
 *
 * @param navController is used to control the screen's state through the overall navigation graph.
 * @param wordViewModel is used to connect the WordViewModel class to the UI from this screen
 */
@Composable
fun TopLevelCorrectWordGameScreen(
    navController: NavHostController,
    wordViewModel: WordViewModel = viewModel()
) {
    val wordsList by wordViewModel.wordsInDatabase.observeAsState(listOf())

    CorrectWordGameScreen(navController = navController, words = wordsList)
}

/**
 * This function represents the screen used to carry out the correct word game based on the
 * native and foreign words found in the Word database.
 *
 * @param navController used to control the navigation state of the screen through the actions
 * carried out by the app.
 * @param words the list which the viewModel uses to connect the words found in the database with
 * the UI from the screen.
 */
@Composable
fun CorrectWordGameScreen(
    navController: NavHostController,
    words: List<Word>
) {
    val context = LocalContext.current
    /*if (words.isNullOrEmpty()){
        navController.navigate(Screen.Home.route)
    }*/
    if (words.isNotEmpty()) {
        val coroutineScope = rememberCoroutineScope()
        val focusManager = LocalFocusManager.current
        var userGuess by rememberSaveable { mutableStateOf("") }
        var isError by rememberSaveable { mutableStateOf(false) }
        var start by rememberSaveable { mutableStateOf(true) }
        var wordPosition by rememberSaveable { mutableStateOf(Random.nextInt(words.size)) }
        val nativeW = words[wordPosition].nativeWord
        val foreignW = words[wordPosition].foreignWord

        ScaffoldCorrectWordGameScreen(
            navController = navController,
            coroutineScope = coroutineScope,
            pageContent = { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                focusManager.clearFocus()
                            })
                        }
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(modifier = Modifier.height(80.dp))

                    Row(
                        modifier = Modifier
                            .padding(start = 40.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = stringResource(id = R.string.correct_word_question),
                            modifier = Modifier,
                            color = MaterialTheme.colorScheme.inversePrimary,
                            fontSize = 20.sp,
                            textAlign = TextAlign.Start,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        modifier = Modifier
                            .padding(start = 40.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(
                            value = nativeW,
                            onValueChange = {},
                            modifier = Modifier,
                            readOnly = true,
                            maxLines = 1,
                            colors = TextFieldDefaults.textFieldColors(
                                textColor = MaterialTheme.colorScheme.primary,
                                containerColor = MaterialTheme.colorScheme.onPrimary,
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        modifier = Modifier
                            .padding(start = 40.dp, end = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        OutlinedTextField(
                            value = userGuess,
                            label = {
                                if (start) {
                                    Text(text = stringResource(R.string.answer_indicator))
                                } else if (!start && !isError) {
                                    Text("The word guessed is right")
                                } else if (!start && isError) {
                                    Text(text = "The word guessed is wrong")
                                }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            onValueChange = { guess ->
                                userGuess = guess
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp),
                            singleLine = true,
                            isError = isError

                        )
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    Row(
                        modifier = Modifier
                            .padding(start = 120.dp, end = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                start = false
                                if (words.isNotEmpty()) {
                                    isError = userGuess != foreignW
                                }
                                focusManager.clearFocus()
                            }
                        ) {
                            Text(text = stringResource(id = R.string.check))
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        modifier = Modifier
                            .padding(start = 135.dp, end = 20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        FilledTonalButton(
                            onClick = {
                                start = true
                                isError = false
                                val num: Int = Random.nextInt(words.size)
                                if(wordPosition == words.lastIndex){
                                    wordPosition = 0
                                } else if (wordPosition != words.lastIndex){
                                    wordPosition += 1
                                }
                                userGuess = ""
                                focusManager.clearFocus()
                            }
                        ) {
                            Text(text = stringResource(id = R.string.move_next_word))
                            Icon(
                                imageVector = Icons.Filled.SkipNext,
                                contentDescription = "next_icon",
                                modifier = Modifier
                                    .padding(start = 3.dp)
                            )
                        }
                    }
                }
            }
        )
    }
}