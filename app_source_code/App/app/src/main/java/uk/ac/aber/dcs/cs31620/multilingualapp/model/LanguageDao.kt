package uk.ac.aber.dcs.cs31620.multilingualapp.model

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * This Data Access Object interface represent the base layer in between the database and the UI
 * from the app. All the actions that might be expected for performing database interactions have to
 * be defined here before the doing so in the Repository or ViewModel. This specifically defines the
 * actions for the LanguageValue class.
 *
 */
@Dao
interface LanguageDao {
    @Insert
    suspend fun insertLanguages(languages: LanguageValue)

    @Insert
    suspend fun insertMultipleLanguages(languagesList: List<LanguageValue>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLanguages(languages: LanguageValue)

    @Delete
    suspend fun deleteLanguages(language: LanguageValue)

    @Query("DELETE FROM languages")
    suspend fun deleteAll()

    @Query("SELECT * FROM languages")
    fun getAllLanguages(): LiveData<List<LanguageValue>>

    @Query("SELECT * FROM languages")
    fun getAllLanguagesSync(): List<LanguageValue>

    @Query("""SELECT * FROM languages WHERE nativeLanguage = :nativeLanguage""")
    fun getTheNativeLanguage(nativeLanguage: String): LiveData<List<LanguageValue>>

    @Query("""SELECT * FROM languages WHERE foreignLanguage = :foreignLanguage""")
    fun getTheForeignLanguage(foreignLanguage: String): LiveData<List<LanguageValue>>

    @Query("SELECT * FROM languages WHERE id = :id")
    fun getTheId(id: Int): LiveData<List<LanguageValue>>
}