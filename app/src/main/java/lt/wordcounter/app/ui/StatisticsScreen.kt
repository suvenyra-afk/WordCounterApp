package lt.wordcounter.app.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import lt.wordcounter.app.data.Repository
import lt.wordcounter.app.ui.charts.DailyChart
import lt.wordcounter.app.ui.charts.HourlyChart
import lt.wordcounter.app.ui.charts.EmotionChart
import java.util.*

@Composable
fun StatisticsScreen(onBack: () -> Unit) {
    val ctx = LocalContext.current
    val repo = remember { Repository(ctx) }
    val scope = rememberCoroutineScope()

    var today by remember { mutableStateOf(0) }
    var days by remember { mutableStateOf(listOf<Pair<String, Int>>()) }
    var selectedDay by remember { mutableStateOf<String?>(null) }
    var hourly by remember { mutableStateOf(listOf<Pair<String, Int>>()) }

    // užkraunam duomenis
    LaunchedEffect(Unit) {
        scope.launch {
            today = repo.getTodayTotal()
            val fromTs = System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000
            days = repo.getDaily(fromTs).map { it.day to it.total }
            if (days.isNotEmpty()) {
                selectedDay = days.last().first
                hourly = repo.getHourly(selectedDay!!).map { it.hour to it.total }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Statistika") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atgal")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text("Šiandien: $today žodžių", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            Text("Paskutinės 30 dienų", style = MaterialTheme.typography.titleMedium)
            DailyChart(days)

            Spacer(modifier = Modifier.height(16.dp))
            Text("Valandų statistika", style = MaterialTheme.typography.titleMedium)
            HourlyChart(hourly)

            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            Text("Emocijų analizė", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))
            EmotionChart()
        }
    }
}
