package lt.wordcounter.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EmotionAnalysisScreen(onBack: () -> Unit) {
    // Testinis duomenų pavyzdys (vėliau galim jungti su realiais rezultatais)
    val emotions = remember {
        mapOf(
            "Laimingas" to 40f,
            "Liūdnas" to 25f,
            "Piktas" to 15f,
            "Pakvaišęs" to 10f,
            "Depresyvus" to 5f,
            "Neutrali" to 5f
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Emocijų analizė", modifier = Modifier.padding(bottom = 16.dp))

        EmotionChart(emotions = emotions)

        Button(
            onClick = { onBack() },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Atgal")
        }
    }
}
