package uk.ac.aber.dcs.cs31620.multilingualapp.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.multilingualapp.model.LanguageDao
import uk.ac.aber.dcs.cs31620.multilingualapp.model.LanguageValue


/**
 * This class shows the way in which the LanguageValue database is created. The database callback
 * function overrides the onCreate method so that a database is created the first time the app is
 * launched. The getDatabase function is synchronized so that any changes within the UI/ViewModel
 * are implemented automatically.
 *
 */
@Database(entities = [LanguageValue::class], version = 1)
abstract class LanguageValueRoomDatabase : RoomDatabase() {

    abstract fun languageDao(): LanguageDao

    companion object {
        private var instance: LanguageValueRoomDatabase? = null
        private val coroutineScope = CoroutineScope(Dispatchers.IO)

        @Synchronized
        fun getDatabase(context: Context): LanguageValueRoomDatabase? {
            if(instance == null){
                instance =
                    Room.databaseBuilder<LanguageValueRoomDatabase>(
                        context.applicationContext,
                        LanguageValueRoomDatabase::class.java,
                        "languages_database"
                    )
                        .allowMainThreadQueries()
                        .addCallback(roomDatabaseCallback(context))
                        //.addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                        .build()
            }
            return instance
        }

        private fun roomDatabaseCallback(context: Context): Callback {
            return object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    coroutineScope.launch {
                        populateDatabase(
                            context,
                            getDatabase(context)!!
                        )
                    }
                }
            }
        }

        private suspend fun populateDatabase(context: Context, instance: LanguageValueRoomDatabase) {
            val languages = LanguageValue(0, "English", "Spanish")

            val languageList = mutableListOf(
                languages
            )

            val dao = instance.languageDao()
            //dao.insertMultipleLanguages(languageList)
        }
    }
}