package uk.ac.aber.dcs.cs31620.multilingualapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull

/**
 * This class defines the values and properties of the Languages data in the database.
 *
 * @param id is the ID of the languages
 * @param nativeLanguage represents where the native language of the user is stored.
 * @param foreignLanguage represents where the foreign language of the user is stored.
 */
@Entity(tableName = "languages")
data class LanguageValue(
@PrimaryKey(autoGenerate = true)
@NotNull
var id: Int = 0,
var nativeLanguage: String = "",
var foreignLanguage: String = ""
)
