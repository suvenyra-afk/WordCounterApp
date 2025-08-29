package lt.wordcounter.app.ui.charts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun EmotionChart(modifier: Modifier = Modifier) {
    val entries = listOf(
        PieEntry(35f, "Laimingas"),
        PieEntry(20f, "Liūdnas"),
        PieEntry(15f, "Piktas"),
        PieEntry(10f, "Įsiutęs"),
        PieEntry(15f, "Ramus"),
        PieEntry(5f, "Depresyvus")
    )

    val dataSet = PieDataSet(entries, "Emocijos")
    dataSet.sliceSpace = 3f
    dataSet.valueTextSize = 14f

    val data = PieData(dataSet)

    AndroidView(
        modifier = modifier,
        factory = { context ->
            PieChart(context).apply {
                this.data = data
                description.isEnabled = false
                legend.orientation = Legend.LegendOrientation.VERTICAL
                legend.isWordWrapEnabled = true
                setEntryLabelTextSize(12f)
                animateY(1000)
            }
        },
        update = { chart ->
            chart.data = data
            chart.invalidate()
        }
    )
}
