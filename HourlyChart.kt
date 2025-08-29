package lt.wordcounter.app.ui.charts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

@Composable
fun HourlyChart(points: List<Pair<String, Int>>, modifier: Modifier = Modifier) {
    AndroidView(modifier = modifier, factory = { ctx ->
        BarChart(ctx).apply {
            description.isEnabled = false
            axisRight.isEnabled = false
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            legend.isEnabled = false
        }
    }, update = { chart ->
        val entries = points.mapIndexed { idx, p -> BarEntry(idx.toFloat(), p.second.toFloat()) }
        val set = BarDataSet(entries, "Per valandą")
        chart.data = BarData(set)
        chart.invalidate()
    })
}