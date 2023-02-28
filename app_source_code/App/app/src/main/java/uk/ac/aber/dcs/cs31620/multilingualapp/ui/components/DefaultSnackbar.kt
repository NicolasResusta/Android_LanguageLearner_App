package uk.ac.aber.dcs.cs31620.multilingualapp.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import uk.ac.aber.dcs.cs31620.multilingualapp.ui.navigation.Screen

/**
 * This function is the construction of what a Snackbar would look like in the app.
 *
 * @param data the data displayed on the snackbar
 * @param modifier used to modify the appearance of the snackbar when displayed in the UI.
 * @param onDismiss this is represents the process that occurs when the action button of the
 * snackbar is pressed.
 */
@Composable
fun DefaultSnackbar(
    data: SnackbarData,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {}
) {
    Snackbar(
        modifier = modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
        content = {
            Text(
                text = data.visuals.message
            )
        },
        action = {
            data.visuals.actionLabel?.let { actionLabel ->
                TextButton(
                    onClick = { onDismiss() }
                ) {
                    Text(
                        text = actionLabel
                    )
                }
            }
        }
    )
}