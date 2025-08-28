package lt.wordcounter.app.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(context: Context) {
    private val dao = AppDatabase.get(context).wordDao()

    suspend fun addWords(timestamp: Long, words: Int) = withContext(Dispatchers.IO) {
        dao.insert(WordCountEntry(timestamp = timestamp, words = words))
    }

    suspend fun cleanup(olderThanTs: Long) = withContext(Dispatchers.IO) {
        dao.deleteOld(olderThanTs)
    }

    suspend fun getDaily(fromTs: Long) = withContext(Dispatchers.IO) {
        dao.getDaily(fromTs)
    }

    suspend fun getHourly(day: String) = withContext(Dispatchers.IO) {
        dao.getHourly(day)
    }

    suspend fun getTodayTotal(): Int = withContext(Dispatchers.IO) {
        dao.getTodayTotal() ?: 0
    }
}