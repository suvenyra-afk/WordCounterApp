package lt.wordcounter.app.ui

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import lt.wordcounter.app.data.Repository
import lt.wordcounter.app.storage.Prefs
import lt.wordcounter.app.ui.charts.DailyChart
import lt.wordcounter.app.ui.charts.HourlyChart
import java.util.*

@Composable
fun MainScreen(
    onStart: ()->Unit,
    onStop: ()->Unit,
    onOpenEmotions: ()->Unit   // <- nauja navigacija į emocijų ekraną
) {
    val ctx = LocalContext.current
    val repo = remember { Repository(ctx) }
    val prefs = remember { Prefs(ctx) }
    val scope = rememberCoroutineScope()

    var today by remember { mutableStateOf(0) }
    var days by remember { mutableStateOf(listOf<Pair<String, Int>>()) }
    var selectedDay by remember { mutableStateOf<String?>(null) }
    var hourly by remember { mutableStateOf(listOf<Pair<String, Int>>()) }
    var wps by remember { mutableStateOf(prefs.wordsPerSecond) }
    var onlyUser by remember { mutableStateOf(prefs.enableSpeakerFilter) }

    LaunchedEffect(Unit) {
        scope.launch {
            today = repo.getTodayTotal()
            val fromTs = System.currentTimeMillis() - 30L*24*60*60*1000
            days = repo.getDaily(fromTs).map { it.day to it.total }
            if (days.isNotEmpty()) {
                selectedDay = days.last().first
                hourly = repo.getHourly(selectedDay!!).map { it.hour to it.total }
            }
        }
    }

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        // viršus
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Šiandien: $today žodžių", style = MaterialTheme.typography.titleLarge)
            Row {
                Button(onClick = onStart) { Text("START") }
                Spacer(Modifier.width(8.dp))
                OutlinedButton(onClick = onStop) { Text("STOP") }
            }
        }

        Spacer(Modifier.height(8.dp))
        Text("Paskutinės 30 dienų", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        DailyChart(days)

        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Diena detaliai:", Modifier.padding(end = 8.dp))
            DropdownDay(days.map { it.first }, selectedDay) { sel ->
                selectedDay = sel
                scope.launch {
                    if (sel != null) hourly = repo.getHourly(sel).map { it.hour to it.total }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        HourlyChart(hourly)

        Spacer(Modifier.height(16.dp))
        // naujas mygtukas emocijoms
        Button(
            onClick = onOpenEmotions,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Emocijų analizė")
        }

        Spacer(Modifier.height(16.dp))
        Text("Nustatymai", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Žodžiai / sekundę: %.2f".format(wps), Modifier.weight(1f))
            Slider(
                value = wps,
                onValueChange = {
                    wps = it
                    prefs.wordsPerSecond = it
                },
                valueRange = 0.5f..5.0f,
                modifier = Modifier.weight(2f)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = onlyUser, onCheckedChange = {
                onlyUser = it
                prefs.enableSpeakerFilter = it
            })
            Text("Skaičiuoti tik mano balsą (eksperimentinė funkcija)")
        }
        if (onlyUser) {
            Button(onClick = { captureVoiceprint(ctx, prefs) }) {
                Text("Užfiksuoti mano balso parašą")
            }
            Text("Patarimas: pasakykite kelias frazes (~10 s), laikydami telefoną įprastu atstumu.")
        }
    }
}

@Composable
private fun DropdownDay(days: List<String>, selected: String?, onChange: (String?)->Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }) { Text(selected ?: "Pasirinkti") }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            days.forEach { d ->
                DropdownMenuItem(text = { Text(d) }, onClick = {
                    onChange(d); expanded = false
                })
            }
        }
    }
}

@Composable
private fun captureVoiceprint(context: Context, prefs: Prefs) {
    Text("Balso parašo fiksavimas: paleiskite START, pakalbėkite 10 s, sustabdykite ir spauskite čia vėl, kad atnaujintumėte.")
}
