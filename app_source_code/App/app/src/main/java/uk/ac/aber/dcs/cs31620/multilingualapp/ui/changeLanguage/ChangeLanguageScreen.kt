package uk.ac.aber.dcs.cs31620.multilingualapp.ui.changeLanguage

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.multilingualapp.R
import uk.ac.aber.dcs.cs31620.multilingualapp.model.LanguageValue
import uk.ac.aber.dcs.cs31620.multilingualapp.model.LanguageValueViewModel
import uk.ac.aber.dcs.cs31620.multilingualapp.model.WordViewModel
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.components.*


/**
 * This function represents the top level used to connect the UI with the view model in order to be
 * able to allow communication with the database. It is also the function to be used to represent
 * the screen in the MainActivity navigation graph.
 *
 * @param navController is used to control the screen's state through the overall navigation graph.
 * @param wordViewModel is used to connect the WordViewModel class to the UI from this screen
 * @param languageViewModel is used to connect the LanguageValueViewModel class to the UI from this
 * screen
 */
@Composable
fun ChangeLanguageScreenTopLevel(
    navController: NavHostController,
    languageViewModel: LanguageValueViewModel = viewModel(),
    wordViewModel: WordViewModel = viewModel()
) {
    val languagesList by languageViewModel.languageInDatabase.observeAsState(listOf())
    val wordsList by wordViewModel.wordsInDatabase.observeAsState(listOf())

    ChangeLanguageScreen(
        navController = navController,
        insertLanguage = { newLanguage ->
            languageViewModel.insert(newLanguage)
        },
        deleteLastLang = { languageToDelete ->
            languageViewModel.deleteLanguage(languageToDelete)
        },
        languageList = languagesList,
        deleteAllWords = {
            wordViewModel.deleteAllWords()
        }
    )
}

/**
 * This function represents the screen used to carry out the language configuration changes based on
 * the native and foreign language found in the Languages database.
 *
 * @param navController used to control the navigation state of the screen through the actions
 * carried out by the app.
 * @param insertLanguage this carries out the insertion of the languages into the database.
 * @param deleteLastLang this carries out the deletion of the last indexed language found in the
 * languages database.
 * @param deleteAllWords this carries out the deletion of all the words present in the words database
 * at the time it is called.
 * @param languageList the list which the viewModel uses to connect the languages found in the
 * database with the UI from the screen.
 */
@Composable
fun ChangeLanguageScreen(
    navController: NavHostController,
    insertLanguage: (LanguageValue) -> Unit = {},
    deleteLastLang: (LanguageValue) -> Unit = {},
    deleteAllWords: () -> Unit = {},
    languageList: List<LanguageValue>
) {
    if (languageList.isNotEmpty()) {
        val coroutineScope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        val focusManager = LocalFocusManager.current
        var userNewNativeLanguage by rememberSaveable { mutableStateOf("") }
        var userNewForeignLanguage by rememberSaveable { mutableStateOf("") }
        val nativeL = languageList[languageList.lastIndex].nativeLanguage
        val foreignL = languageList[languageList.lastIndex].foreignLanguage
        val openDialog = remember { mutableStateOf(false) }
        val stateOfDialogFunction = rememberSaveable { mutableStateOf(false) }


        ScaffoldLanguageChangeScreen(
            navController = navController,
            coroutineScope = coroutineScope,
            snackbarHostState = snackbarHostState,
            snackbarContent = { data ->
                DefaultSnackbar(
                    data = data,
                    modifier = Modifier.padding(bottom = 4.dp),
                    onDismiss = {
                        data.dismiss()
                    }
                )
            },
            pageContent = { innerPadding ->
                Column(
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .pointerInput(Unit) {
                            detectTapGestures(onTap = {
                                focusManager.clearFocus()
                            })
                        }
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.Start
                ) {
                    ConstraintLayout(
                        modifier = Modifier
                    ) {
                        val (title1, box1, title2, box2) = createRefs()

                        Text(
                            text = "Current Native Language",
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                                .focusable()
                                .constrainAs(title1) {
                                    start.linkTo(parent.start)
                                    top.linkTo(parent.top)
                                    bottom.linkTo(parent.bottom)
                                },
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = nativeL,
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp, bottom = 30.dp)
                                .focusable()
                                .constrainAs(box1) {
                                    top.linkTo(title1.bottom)
                                },
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = "Current Foreign Language",
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
                                .focusable()
                                .constrainAs(title2) {
                                    top.linkTo(box1.bottom)
                                },
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            text = foreignL,
                            modifier = Modifier
                                .padding(start = 20.dp, end = 20.dp, bottom = 30.dp)
                                .focusable()
                                .constrainAs(box2) {
                                    top.linkTo(title2.bottom)
                                },
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                    ) {
                        OutlinedTextField(
                            value = userNewNativeLanguage,
                            label = {
                                Text(text = stringResource(R.string.language_page_message1))
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            onValueChange = { languageValue ->
                                userNewNativeLanguage = languageValue
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(15.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                    ) {
                        OutlinedTextField(
                            value = userNewForeignLanguage,
                            label = {
                                Text(text = stringResource(R.string.language_page_message2))
                            },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            onValueChange = { languageValue ->
                                userNewForeignLanguage = languageValue
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 8.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(30.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(start = 20.dp, end = 20.dp)
                    ) {
                        Button(
                            onClick = {
                                if (userNewNativeLanguage == "" || userNewForeignLanguage == "") {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(
                                            message = "If the input languages are empty, the " +
                                                    "operation won't proceed.",
                                        )
                                    }
                                } else {
                                    openDialog.value = true
                                }
                            },
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .weight(1f)
                        ) {
                            Text(text = stringResource(id = R.string.submit))
                            if (openDialog.value) {
                                AlertDialog(
                                    onDismissRequest = {
                                    },
                                    title = {
                                        Text(text = stringResource(id = R.string.alert_dialog_Title))
                                    },
                                    text = {
                                        Text(text = stringResource(id = R.string.alert_dialog_Text))
                                    },
                                    confirmButton = {
                                        TextButton(
                                            onClick = {
                                                stateOfDialogFunction.value = true
                                            }
                                        ) {
                                            Text("Confirm")
                                            if (stateOfDialogFunction.value) {
                                                openDialog.value = false
                                                languageChange(
                                                    nativeLanguage = userNewNativeLanguage,
                                                    foreignLanguage = userNewForeignLanguage,
                                                    doAction = { newLanguage ->
                                                        deleteLastLang(languageList[languageList.lastIndex])
                                                        insertLanguage(newLanguage)
                                                        deleteAllWords()
                                                    }
                                                )
                                                userNewNativeLanguage = ""
                                                userNewForeignLanguage = ""
                                                focusManager.clearFocus()
                                                stateOfDialogFunction.value = false
                                            }
                                        }
                                    },
                                    dismissButton = {
                                        TextButton(
                                            onClick = {
                                                openDialog.value = false
                                            }
                                        ) {
                                            Text("Cancel")
                                        }
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(30.dp))

                        Button(
                            onClick = {
                                userNewNativeLanguage = ""
                                userNewForeignLanguage = ""
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
        )
    }
}

/**
 * This function is used to implement the viewModel functions within the UI
 *
 * @param nativeLanguage the native language to perform the action with.
 * @param foreignLanguage the foreign language to perform the action with.
 * @param doAction the action which uses the native and foreign languages to get carried out.
 */

private fun languageChange(
    nativeLanguage: String,
    foreignLanguage: String,
    doAction: (LanguageValue) -> Unit = {}
) {
    val language = LanguageValue(
        id = 0,
        nativeLanguage = nativeLanguage,
        foreignLanguage = foreignLanguage
    )
    doAction(language)
}