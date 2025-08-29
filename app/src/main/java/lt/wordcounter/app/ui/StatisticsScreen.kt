package lt.wordcounter.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lt.wordcounter.app.ui.charts.EmotionChart

@Composable
fun StatisticsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emocijų statistika") },
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
            Text("Čia parodyta emocijų analizė:", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(16.dp))

            // Diagramai panaudosime jau sukurtą komponentą
            EmotionChart()
        }
    }
}
