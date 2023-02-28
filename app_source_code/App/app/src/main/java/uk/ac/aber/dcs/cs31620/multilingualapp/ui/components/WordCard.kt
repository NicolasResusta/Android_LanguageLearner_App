package uk.ac.aber.dcs.cs31620.multilingualapp.ui.components


import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import uk.ac.aber.dcs.cs31620.multilingualapp.R
import uk.ac.aber.dcs.cs31620.multilingualapp.model.Word

/**
 * This function shows how the wordCards used to display words in the VocabList screen are formed
 * and the actions in them.
 *
 * @param modifier used to modify the appearance of the wordCard when displayed in the UI.
 * @param word it is a Word object used to determine where fields of the entity go.
 * @param deleteAction this is a lambda which is used to carry out a deletion of a word.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordCard(
    modifier: Modifier = Modifier,
    word: Word,
    deleteAction: (Word) -> Unit = {}
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .fillMaxWidth()
    ) {
        ConstraintLayout {
            val (word1Ref, word2Ref, editRef) = createRefs()

            Text(
                text = word.nativeWord,
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp)
                    .constrainAs(word1Ref) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)

                    },
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Start
            )

            Text(
                text = word.foreignWord,
                modifier = Modifier
                    .padding(start = 230.dp)
                    .constrainAs(word2Ref) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)

                    },
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.End
            )

            IconButton(
                onClick = { deleteAction(word) },
                modifier = Modifier
                    .padding(start = 320.dp, end = 5.dp)
                    .constrainAs(editRef) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            ) {
              Icon(
                  imageVector = Icons.Filled.Delete,
                  contentDescription = stringResource(id = R.string.delete_button))
            }

            /*ButtonSpinner(
                items = optionsList,
                itemClick = { updateAction(word) },
                modifier = Modifier
                    .padding(start = 320.dp, end = 5.dp)
                    .constrainAs(editRef) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }

            )*/
        }
    }
}

