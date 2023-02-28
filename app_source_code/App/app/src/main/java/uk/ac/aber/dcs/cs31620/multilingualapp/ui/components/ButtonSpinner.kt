package uk.ac.aber.dcs.cs31620.multilingualapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import uk.ac.aber.dcs.cs31620.multilingualapp.R
import uk.ac.aber.dcs.cs31620.multilingualapp.model.Word
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.theme.MultinlingualAppTheme

/**
 * This function represents the options shown within a dropdown list when the MoreVert button is
 * pressed.
 *
 * @param items the items that are going to be displayed after the button is pressed.
 * @param itemClick the action that happens when a certain item from the items list is pressed.
 * @param modifier the modifier qualities applied to the UI screen.
 */
@Composable
fun ButtonSpinner(
    items: List<String>,
    itemClick: () -> Unit = {},
    modifier: Modifier = Modifier
){
    var expanded by rememberSaveable { mutableStateOf(false) }
    IconButton(
        onClick = { expanded = !expanded },
        modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(id = R.string.editIcon)
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            items.forEach {
                DropdownMenuItem(
                    text = { Text(text = it)},
                    onClick = {
                        expanded = false
                        itemClick()
                    }
                )
            }
        }
    }
}

@Composable
@Preview
private fun ButtonSpinnerPreview() {
    MultinlingualAppTheme(dynamicColor = false) {
        val items = listOf("Numbers", "1", "2")
        ButtonSpinner(items = items)
    }
}