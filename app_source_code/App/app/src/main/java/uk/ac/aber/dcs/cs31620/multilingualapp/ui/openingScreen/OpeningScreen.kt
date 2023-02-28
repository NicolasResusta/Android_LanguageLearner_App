package uk.ac.aber.dcs.cs31620.multilingualapp.ui.openingScreen

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.multilingualapp.R
import uk.ac.aber.dcs.cs31620.multilingualapp.model.LanguageValue
import uk.ac.aber.dcs.cs31620.multilingualapp.model.LanguageValueViewModel
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.components.DefaultSnackbar
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.components.ScaffoldOpeningScreen
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.theme.MultinlingualAppTheme

/**
 * This function represents the top level used to connect the UI with the view model in order to be
 * able to allow communication with the database. It is also the function to be used to represent
 * the screen in the MainActivity navigation graph.
 *
 * @param navController is used to control the screen's state through the overall navigation graph.
 * @param languageValueViewModel is used to connect the LanguageValueViewModel class to the UI
 * from this screen.
 */
@Composable
fun OpeningScreenTopLevel(
    navController: NavHostController,
    languageValueViewModel: LanguageValueViewModel = viewModel()
) {
    val languageList by languageValueViewModel.languageInDatabase.observeAsState(listOf())

    OpeningScreen(
        navController = navController,
        insertLanguage = { languageValue ->
            languageValueViewModel.insert(languageValue)
        }
    )

}

/**
 * This function shows the construction of the UI and actions which is shown when the user has first
 * opened the app and the databases are empty.
 *
 * @param navController used to control the navigation state of the screen through the actions
 * carried out by the app.
 * @param insertLanguage this carries out the insertion of the languages into the database.
 */
@Composable
fun OpeningScreen(
    navController: NavHostController,
    //languageList: List<LanguageValue>,
    insertLanguage: (LanguageValue) -> Unit = {}
    //deleteLastLang: (LanguageValue) -> Unit = {}
) {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var userNativeLang by rememberSaveable { mutableStateOf("") }
    var userForeignLang by rememberSaveable { mutableStateOf("") }
    val focusManager = LocalFocusManager.current


    ScaffoldOpeningScreen(
        navController = navController,
        coroutineScope = coroutineScope,
        pageContent = { innerPadding ->

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
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
                        value = userNativeLang,
                        label = {
                            Text(text = stringResource(R.string.opening_page_message1))
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        onValueChange = { languageValue ->
                            userNativeLang = languageValue
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(start = 20.dp, end = 20.dp)
                ) {
                    OutlinedTextField(
                        value = userForeignLang,
                        label = {
                            Text(text = stringResource(R.string.opening_page_message2))
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        onValueChange = { languageValue ->
                            userForeignLang = languageValue
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
                            languageSetup(
                                nativeLanguage = userNativeLang,
                                foreignLanguage = userForeignLang,
                                doAction = { newLanguage ->
                                    //deleteLastLang(languageList[languageList.lastIndex])
                                    insertLanguage(newLanguage)
                                }
                            )
                            focusManager.clearFocus()
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
                            userNativeLang = ""
                            userForeignLang = ""
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

/**
 * This function is used to implement the viewModel functions within the UI
 *
 * @param nativeLanguage the native language to perform the action with.
 * @param foreignLanguage the foreign language to perform the action with.
 * @param doAction the action which uses the native and foreign languages to get carried out.
 */
private fun languageSetup(
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


@Composable
@Preview
private fun OpeningScreenPreview() {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    MultinlingualAppTheme(dynamicColor = false) {
        //OpeningScreen(navController,)
    }
}


