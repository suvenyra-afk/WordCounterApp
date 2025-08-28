package lt.wordcounter.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word_count")
data class WordCountEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val words: Int
)