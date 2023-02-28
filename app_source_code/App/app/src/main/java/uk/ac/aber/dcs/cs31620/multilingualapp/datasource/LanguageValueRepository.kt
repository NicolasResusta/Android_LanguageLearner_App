package uk.ac.aber.dcs.cs31620.multilingualapp.datasource

import android.app.Application
import uk.ac.aber.dcs.cs31620.multilingualapp.model.LanguageValue

/**
 * This class represents the repository needed to implement the insertions/queries/deletions from
 * the LanguageValue Dao
 *
 * @param application is used to relate the repository to the app
 */
class LanguageValueRepository (application: Application) {
    private val languageDao = LanguageValueRoomDatabase.getDatabase(application)!!.languageDao()

    suspend fun insertLanguages(languages: LanguageValue){
        languageDao.insertLanguages(languages)
    }

    suspend fun insertMultipleLanguages(languagesList: List<LanguageValue>){
        languageDao.insertMultipleLanguages(languagesList)
    }

    suspend fun updateLanguages(languages: LanguageValue){
        languageDao.updateLanguages(languages)
    }

    suspend fun deleteLanguages(language: LanguageValue){
        languageDao.deleteLanguages(language)
    }

    suspend fun deleteAll() = languageDao.deleteAll()

    fun getAllLanguages() = languageDao.getAllLanguages()

    fun getTheId(id: Int){
        languageDao.getTheId(id)
    }

    fun getTheNativeLanguage(nativeLanguage: String){
        languageDao.getTheNativeLanguage(nativeLanguage)
    }

    fun getTheForeignLanguage(foreignLanguage: String){
        languageDao.getTheForeignLanguage(foreignLanguage)
    }

    fun getAllLanguagesSync() = languageDao.getAllLanguagesSync()
}