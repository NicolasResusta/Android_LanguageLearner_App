package uk.ac.aber.dcs.cs31620.multilingualapp.model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.multilingualapp.datasource.LanguageValueRepository

/**
 * This class represents the main layer which communicates between the UI and the database. The
 * functions in it are extracted from the LanguageValueRepository class.
 *
 * @param application is the connection made to allow changes to the database from the app itself.
 */
class LanguageValueViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: LanguageValueRepository = LanguageValueRepository(application)

    var languageInDatabase: LiveData<List<LanguageValue>> = repository.getAllLanguages()
        private set

    fun insert(newLanguage: LanguageValue){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertLanguages(newLanguage)
        }
    }

    fun deleteLanguage(languageValue: LanguageValue){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteLanguages(languageValue)
        }
    }

    fun deleteAllLanguages(){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteAll()
        }
    }

    fun updateLanguages(languageValue: LanguageValue){
        viewModelScope.launch (Dispatchers.IO){
            repository.updateLanguages(languageValue)
        }
    }

    fun getAllLanguages(){
        viewModelScope.launch (Dispatchers.IO){
            repository.getAllLanguages()
        }
    }

    fun getTheId(id: Int){
        repository.getTheId(id)
    }

    fun getTheNativeLanguage(nativeLanguage: String){
        repository.getTheNativeLanguage(nativeLanguage)
    }

    fun getTheForeignLanguage(foreignLanguage: String){
        repository.getTheForeignLanguage(foreignLanguage)
    }
}