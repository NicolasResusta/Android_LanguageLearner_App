package uk.ac.aber.dcs.cs31620.multilingualapp.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.multilingualapp.R
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.navigation.Screen
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.theme.MultinlingualAppTheme

/**
 * This function covers the display and functionality of the navigation drawer used in the app.
 *
 * @param navController used to control the navigation state of the screen through the actions
 * carried out by the app.
 * @param drawerState this state variable determines whether the drawer should be opened or closed.
 * @param closeDrawer This shows the action of closing the drawer after the item is selected.
 * @param content this is the content displayed on the navigation drawer shown as a composable
 * function.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainPageNavigationDrawer(
    navController: NavHostController,
    drawerState: DrawerState,
    closeDrawer: () -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    val items = listOf(
        Pair(
            Icons.Default.VideogameAsset,
            stringResource(R.string.find_correct_word_title)
        ),
        Pair(
            Icons.Default.VideogameAsset,
            stringResource(R.string.find_word_from_anagram_title)
        ),
        /*Pair(
            Icons.Default.VideogameAsset,
            stringResource(R.string.find_hang_man_title)
        ),*/
        Pair(
            Icons.Default.Settings,
            stringResource(R.string.change_lang_screen_title)
        )
    )

    ModalNavigationDrawer(

        drawerState = drawerState,
        drawerContent = {
            val selectedItem = rememberSaveable { mutableStateOf(5) }
            // This is set to 5 so that no item is selected by default as it looks annoying on screen
            // it always has to be bigger than the number of items.
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Text(
                //This is to have some top padding, as the one given in the item modifier wasn't great
                //cause I needed a general one, not for every item on the list, and spacers were too much
                    "",
                    fontSize = 2.sp,
                    modifier = Modifier
                        .padding(top = 7.dp, bottom = 5.dp))

                items.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = item.first,
                                contentDescription = item.second
                            )
                        },
                        label = { Text(item.second) },

                        selected = index == selectedItem.value,
                        onClick = {

                            selectedItem.value = index

                            when (index) {
                                0 -> {
                                    closeDrawer()
                                    navController.navigate(route = Screen.CorrectWordGameScreen.route)
                                }
                                1 -> {
                                    closeDrawer()
                                    navController.navigate(route = Screen.AnagramGameScreen.route)
                                }
                                2 -> {
                                    closeDrawer()
                                    navController.navigate(route = Screen.LanguageChangeScreen.route)
                                }
                                /*3 -> {
                                    closeDrawer()
                                    navController.navigate(route = Screen.HangmanGameScreen.route)
                                }*/
                            }
                        },
                        modifier = Modifier
                            .padding(top = 2.dp, bottom = 2.dp, end = 10.dp),
                    )
                }
            }
        },
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
private fun NavDrawerPreview(){
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Open)
    MultinlingualAppTheme(dynamicColor = false) {
        MainPageNavigationDrawer(navController = navController, drawerState = drawerState)
    }
}