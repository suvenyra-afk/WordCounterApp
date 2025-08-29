package lt.wordcounter.app

import android.graphics.Color
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

/**
 * Pyrago diagrama emocijoms (MPAndroidChart).
 * Pavyzdys: mapOf("Laimingas" to 40f, "Liūdnas" to 25f, "Piktas" to 15f, "Neutrali" to 10f)
 */
@Composable
fun EmotionChart(emotions: Map<String, Float>) {
    AndroidView(
        factory = { ctx ->
            PieChart(ctx).apply {
                description.isEnabled = false
                isDrawHoleEnabled = true
                holeRadius = 45f
                setUsePercentValues(true)
                setDrawEntryLabels(true)
                setEntryLabelColor(Color.BLACK)
                legend.isEnabled = true
            }
        },
        update = { chart ->
            val entries = emotions.entries.map { (label, value) ->
                PieEntry(value, label)
            }

            val dataSet = PieDataSet(entries, "Emocijų analizė").apply {
                // Pastelinių atspalvių paletė
                colors = listOf(
                    Color.rgb(76, 175, 80),    // Laimingas - žalia
                    Color.rgb(33, 150, 243),   // Liūdnas - mėlyna
                    Color.rgb(244, 67, 54),    // Piktas - raudona
                    Color.rgb(255, 152, 0),    // Pakvaišęs - oranžinė
                    Color.rgb(96, 125, 139),   // Depresyvus - pilkai melsva
                    Color.rgb(158, 158, 158)   // Neutrali - pilka
                )
                valueTextColor = Color.BLACK
                valueTextSize = 12f
                sliceSpace = 2f
            }

            chart.data = PieData(dataSet).apply {
                setValueTextSize(12f)
                setValueTextColor(Color.BLACK)
            }
            chart.invalidate()
        }
    )
}
