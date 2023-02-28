package uk.ac.aber.dcs.cs31620.multilingualapp.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * This function represents the scaffolding arrangement for the home and vocab list screen.
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
fun TopLevelScaffold(
    navController: NavHostController,
    floatingActionButton: @Composable () -> Unit = {},
    coroutineScope: CoroutineScope,
    snackbarHostState: SnackbarHostState? = null,
    pageContent: @Composable (innerPadding: PaddingValues) -> Unit = {},
    snackbarContent: @Composable (SnackbarData) -> Unit = {}
){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()

    MainPageNavigationDrawer(
        navController,
        drawerState = drawerState,
        closeDrawer = {
            coroutineScope.launch {
                drawerState.close()
            }
        }
    ) {
        Scaffold(
            topBar = {
                MainPageTopAppBar(
                    onClick = {
                        coroutineScope.launch {
                            if (drawerState.isOpen) {
                                drawerState.close()
                            } else {
                                drawerState.open()
                            }
                        }
                    }
                )
            },
            bottomBar = {
                MainPageNavigationBar(navController)
            },
            floatingActionButton = floatingActionButton,
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
}
