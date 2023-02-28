package uk.ac.aber.dcs.cs31620.multilingualapp.ui.components

import androidx.compose.ui.graphics.vector.ImageVector

/**
 * This class defines the the features an Icon has when displayed.
 *
 * @param filledIcon
 * @param outlineIcon
 * @param label
 */
data class IconGroup(
    val filledIcon: ImageVector,
    val outlineIcon: ImageVector,
    val label: String
)
