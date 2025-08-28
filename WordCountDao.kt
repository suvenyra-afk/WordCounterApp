package lt.wordcounter.app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WordCountDao {
    @Insert
    suspend fun insert(entry: WordCountEntry)

    @Query("DELETE FROM word_count WHERE timestamp < :limit")
    suspend fun deleteOld(limit: Long)

    // Last 30 days by day
    @Query("""
        SELECT strftime('%Y-%m-%d', timestamp/1000, 'unixepoch', 'localtime') as day,
               SUM(words) as total
        FROM word_count
        WHERE timestamp >= :fromTs
        GROUP BY day
        ORDER BY day ASC
    """)
    suspend fun getDaily(fromTs: Long): List<DailyStat>

    // For a specific day: by hour
    @Query("""
        SELECT strftime('%H', timestamp/1000, 'unixepoch', 'localtime') as hour,
               SUM(words) as total
        FROM word_count
        WHERE strftime('%Y-%m-%d', timestamp/1000, 'unixepoch', 'localtime') = :day
        GROUP BY hour
        ORDER BY hour ASC
    """)
    suspend fun getHourly(day: String): List<HourlyStat>

    @Query("""
        SELECT SUM(words) FROM word_count
        WHERE strftime('%Y-%m-%d', timestamp/1000, 'unixepoch', 'localtime') = strftime('%Y-%m-%d','now','localtime')
    """)
    suspend fun getTodayTotal(): Int?
}

data class DailyStat(val day: String, val total: Int)
data class HourlyStat(val hour: String, val total: Int)