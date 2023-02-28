package uk.ac.aber.dcs.cs31620.multilingualapp.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import uk.ac.aber.dcs.cs31620.multilingualapp.R
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.navigation.Screen

/**
 * This function represents the scaffolding arrangement for the hangman game screen.
 *
 * @param navController used to control the navigation state of the screen through the actions
 * carried out by the app.
 * @param coroutineScope this is used to launch and end coroutines within the scaffold.
 * @param snackbarHostState this used to hold the state of the snackbar.
 * @param pageContent this is a composable lambda function which holds the content of the page.
 * @param snackbarContent this is a composable function which holds the content of the snackbar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ScaffoldWordFinderGameScreen(
    navController: NavHostController,
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState? = null,
    pageContent: @Composable (innerPadding: PaddingValues) -> Unit = {},
    snackbarContent: @Composable (SnackbarData) -> Unit = {}
){


    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.word_finder_title),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(start = 30.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(route = Screen.Home.route) }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.arrow_icon)
                        )
                    }
                }
            )
        },
        snackbarHost = {
            snackbarHostState?.let {
                SnackbarHost(hostState = snackbarHostState) {data ->
                    snackbarContent(data)
                }
            }
        },
        content = { innerPadding -> pageContent(innerPadding) }
    )
}