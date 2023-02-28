package uk.ac.aber.dcs.cs31620.multilingualapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

/**
 * This class defines the values and properties of the Word data in the database.
 *
 * @param id is the ID of the words
 * @param nativeWord represents where the native word the user is stored.
 * @param foreignWord represents where the foreign word of the user is stored.
 */
@Entity(tableName = "words_table")
data class Word(
    @PrimaryKey(autoGenerate = true)
    @NotNull
    var id: Int = 0,
    var nativeWord: String = "",
    var foreignWord: String = ""
)