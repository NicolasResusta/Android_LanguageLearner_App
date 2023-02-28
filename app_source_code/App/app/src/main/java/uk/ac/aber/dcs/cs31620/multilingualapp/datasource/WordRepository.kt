package uk.ac.aber.dcs.cs31620.multilingualapp.datasource

import android.app.Application
import uk.ac.aber.dcs.cs31620.multilingualapp.model.Word

/**
 * This class represents the repository needed to implement the insertions/queries/deletions from
 * the Word Dao
 *
 * @param application is used to relate the repository to the app
 */
class WordRepository(application: Application) {
    private val wordDao = WordRoomDatabase.getDatabase(application)!!.wordDao()

    suspend fun insert(word: Word){
        wordDao.insertSingleWord(word)
    }

    suspend fun insertMultipleWords(words: List<Word>){
        wordDao.insertMultipleWords(words)
    }

    suspend fun deleteWord(word: Word){
        wordDao.deleteWord(word)
    }

    suspend fun deleteAll() = wordDao.deleteAll()

    suspend fun updateWord(word: Word){
        wordDao.updateWord(word)
    }

    fun getAllWords() = wordDao.getAllWords()

    fun getWord(nativeWord: String, foreignWord: String){
        wordDao.getWord(nativeWord, foreignWord)
    }

    fun getNativeWord(nativeWord: String){
        wordDao.getNativeWord(nativeWord)
    }

    fun getForeignWord(foreignWord: String){
        wordDao.getForeignWord(foreignWord)
    }

    /*fun getNativeWords(nativeWordsList: List<Word>){
        wordDao.getNativeWords(nativeWordsList)
    }

    fun getForeignWords(foreignWordsList: List<Word>){
        wordDao.getForeignWords(foreignWordsList)
    }*/

    fun getId(id: Int){
        wordDao.getId(id)
    }

    fun getWordsSync() = wordDao.getAllWordsSync()
}