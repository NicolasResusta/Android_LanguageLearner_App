package uk.ac.aber.dcs.cs31620.multilingualapp.model

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.multilingualapp.datasource.WordRepository
import uk.ac.aber.dcs.cs31620.multilingualapp.model.*
import kotlin.random.Random

/**
 * This class represents the main layer which communicates between the UI and the database. The
 * functions in it are extracted from the WordRepository class.
 *
 * @param application is the connection made to allow changes to the database from the app itself.
 */
class WordViewModel(application: Application): AndroidViewModel(application) {
    private val repository: WordRepository = WordRepository(application)

    var wordsInDatabase: LiveData<List<Word>> = repository.getAllWords()
        private set


    fun insertWord(newWord: Word){
        viewModelScope.launch(Dispatchers.IO){
            repository.insert(newWord)
        }
    }

    fun updateWord(newWord: Word){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateWord(newWord)
        }
    }

    fun deleteWord(selectedWord: Word){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteWord(selectedWord)
        }
    }

    fun deleteAllWords(){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteAll()
        }
    }

    fun getSpecificNativeWord(nativeWord: String){
        repository.getNativeWord(nativeWord)
    }

    fun getSpecificForeignWord(foreignWord: String){
        repository.getForeignWord(foreignWord)
    }

    fun getWord(native: String, foreign: String){
        repository.getWord(native, foreign)
    }
}