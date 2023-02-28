package uk.ac.aber.dcs.cs31620.multilingualapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import uk.ac.aber.dcs.cs31620.multilingualapp.R

/**
 * This function shows the display for the top app bar in the home and vocab list screens.
 *
 * @param onClick this serves as a button which expands the navigation drawer if pressed.
 */
@Composable
fun MainPageTopAppBar(onClick: () -> Unit = {}){
    CenterAlignedTopAppBar(
        title = {
            Text(stringResource(id = R.string.app_name))
        },
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription =
                    stringResource(R.string.nav_drawer_menu)
                )
            }
        }
    )
}