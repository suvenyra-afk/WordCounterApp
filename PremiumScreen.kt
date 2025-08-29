package lt.wordcounter.app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PremiumScreen(premiumManager: PremiumManager) {
    var code by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Įveskite Premium kodą")

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = code,
            onValueChange = { code = it },
            label = { Text("Kodas") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(onClick = {
            val success = premiumManager.redeemCode(code)
            message = if (success) "Premium aktyvuotas ✅" else "Neteisingas kodas ❌"
        }) {
            Text("Aktyvuoti")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(message)
    }
}
