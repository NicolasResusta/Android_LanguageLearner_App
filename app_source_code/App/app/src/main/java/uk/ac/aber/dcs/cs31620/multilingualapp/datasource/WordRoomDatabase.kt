package uk.ac.aber.dcs.cs31620.multilingualapp.datasource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uk.ac.aber.dcs.cs31620.multilingualapp.model.Word
import uk.ac.aber.dcs.cs31620.multilingualapp.model.WordDao


/**
 * This class shows the way in which the Word database is created. The database callback
 * function overrides the onCreate method so that a database is created the first time the app is
 * launched. The getDatabase function is synchronized so that any changes within the UI/ViewModel
 * are implemented automatically.
 *
 */
@Database(entities = [Word::class], version = 1)
abstract class WordRoomDatabase : RoomDatabase() {

    abstract fun wordDao(): WordDao

    companion object{
        private var instance: WordRoomDatabase? = null
        private val coroutineScope = CoroutineScope(Dispatchers.IO)

        @Synchronized
        fun getDatabase(context: Context): WordRoomDatabase? {
            if(instance == null){
                instance =
                    Room.databaseBuilder<WordRoomDatabase>(
                        context.applicationContext,
                        WordRoomDatabase::class.java,
                        "word_database"
                    )
                        .allowMainThreadQueries()
                        .addCallback(roomDatabaseCallback(context))
                        .build()
            }
            return instance
        }

        private fun roomDatabaseCallback(context: Context): Callback {
            return object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    coroutineScope.launch {
                        populateDatabase(context, getDatabase(context)!!)
                    }
                }
            }
        }

        private suspend fun populateDatabase(context: Context, instance: WordRoomDatabase) {
            val word1 = Word(0, "rain", "lluvia")
            val word2 = Word(0, "water", "agua")

            val wordList = mutableListOf(
                word1,
                word2
            )

            val dao = instance.wordDao()
            //dao.insertMultipleWords(wordList)
        }
    }
}