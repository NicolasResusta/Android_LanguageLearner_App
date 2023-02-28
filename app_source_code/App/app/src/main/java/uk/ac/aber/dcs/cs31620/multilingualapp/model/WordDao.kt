package uk.ac.aber.dcs.cs31620.multilingualapp.model

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * This Data Access Object interface represent the base layer in between the database and the UI
 * from the app. All the actions that might be expected for performing database interactions have to
 * be defined here before the doing so in the Repository or ViewModel. This specifically defines the
 * actions for the Word class.
 *
 */
@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSingleWord(word: Word)

    @Insert
    suspend fun insertMultipleWords(wordList: List<Word>)

    @Delete
    suspend fun deleteWord(word: Word)

    @Update(entity = Word::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateWord(word: Word)

    @Query("DELETE FROM words_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM words_table")
    fun getAllWords(): LiveData<List<Word>>

    @Query("SELECT * FROM words_table")
    fun getAllWordsSync(): List<Word>

    @Query("SELECT * FROM words_table WHERE nativeWord = :nativeWord AND foreignWord = :foreignWord")
    fun getWord(nativeWord: String, foreignWord: String): LiveData<List<Word>>

    @Query("SELECT * FROM words_table WHERE nativeWord = :nativeWord")
    fun getNativeWord(nativeWord: String): LiveData<List<Word>>

    @Query("SELECT * FROM words_table WHERE foreignWord = :foreignWord")
    fun getForeignWord(foreignWord: String): LiveData<List<Word>>

    @Query("SELECT * FROM words_table WHERE id = :id")
    fun getId(id: Int): LiveData<List<Word>>

    /*@Query("SELECT nativeWord FROM words_table")
    fun getNativeWords(nativeWList: List<Word>): LiveData<List<Word>>

    @Query("SELECT foreignWord FROM words_table")
    fun getForeignWords(foreignWList: List<Word>): LiveData<List<Word>>*/
}