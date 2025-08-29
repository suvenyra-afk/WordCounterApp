package lt.wordcounter.app.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lt.wordcounter.app.ui.charts.EmotionChart

@Composable
fun EmotionsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Emocijų analizė") },
                navigationIcon = {
                    Button(onClick = onBack) { Text("Atgal") }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            Text(
                "Čia matysi savo emocijų statistiką pagal balsą",
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.height(16.dp))

            // mūsų emocijų diagrama
            EmotionChart()
        }
    }
}
