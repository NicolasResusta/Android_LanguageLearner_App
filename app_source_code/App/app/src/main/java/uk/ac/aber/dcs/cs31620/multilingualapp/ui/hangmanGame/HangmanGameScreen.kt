package uk.ac.aber.dcs.cs31620.multilingualapp.ui.hangmanGame

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uk.ac.aber.dcs.cs31620.multilingualapp.R
import uk.ac.aber.dcs.cs31620.multilingualapp.model.Word
import uk.ac.aber.dcs.cs31620.multilingualapp.model.WordViewModel
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.components.ScaffoldWordFinderGameScreen
import kotlin.random.Random

@Composable
fun TopLevelHangmanGameScreen(
    navController: NavHostController,
    wordViewModel: WordViewModel = viewModel()
) {
    val wordsList by wordViewModel.wordsInDatabase.observeAsState(listOf())

    HangmanGameScreen(navController = navController, words = wordsList)
}

@Composable
fun HangmanGameScreen(
    navController: NavHostController,
    words: List<Word>
) {
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    var userGuess by rememberSaveable { mutableStateOf("") }
    var wordPosition by rememberSaveable { mutableStateOf(Random.nextInt(words.size)) }
    val foreignW = words[wordPosition].foreignWord
    var isError by rememberSaveable { mutableStateOf(false) }
    var start by rememberSaveable { mutableStateOf(true) }
    val letters = foreignW.toCharArray().toMutableList()
    val correctGuesses = MutableList(letters.size) { '_' }
    var incorrectGuesses = 0

    ScaffoldWordFinderGameScreen(
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
            ) {

                Spacer(modifier = Modifier.height(40.dp))

                Row(
                    modifier = Modifier
                        .padding(start = 20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = stringResource(id = R.string.word_finder_body),
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.inversePrimary,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Start,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                /*Row(
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
                }*/

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .padding(start = 40.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    /*Image(
                        painter = painterResource(id = R.drawable.word_puzzle),
                        contentDescription = "puzzle_image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .clickable {
                                Toast.makeText(context, "Touched image", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            .size(height = 300.dp, width = 300.dp)
                    )*/

                    OutlinedTextField(
                        value = userGuess,
                        label = {
                            Text(text = stringResource(R.string.answer_indicator))
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

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .padding(start = 120.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    Button(onClick = {
                        focusManager.clearFocus()
                    }) {
                        Text(text = stringResource(id = R.string.check))
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                Row(
                    modifier = Modifier
                        .padding(start = 150.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ){
                    FilledTonalButton(onClick = { focusManager.clearFocus()}) {
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

    // Start the game loop
    while (incorrectGuesses < 6) {
        // Print the current state of the game
        println("Incorrect guesses: $incorrectGuesses")
        println("Word: ${correctGuesses.joinToString(" ")}")

        // Get the next guess from the player
        print("Enter a letter: ")
        val guess = readLine()!!.first()

        // Update the game state based on the guess
        if (guess in letters) {
            // The guess was correct, update the correct guesses list
            val index = letters.indexOf(guess)
            correctGuesses[index] = guess
            letters[index] = '_'
        } else {
            // The guess was incorrect, increment the incorrect guess count
            incorrectGuesses++
        }

        // Check if the player has won
        if (correctGuesses.none { it == '_' }) {
            println("Congratulations, you won!")
            break
        }
    }

    // Check if the player has lost
    if (incorrectGuesses == 6) {
        println("Sorry, you lost. The word was $foreignW")
    }
}