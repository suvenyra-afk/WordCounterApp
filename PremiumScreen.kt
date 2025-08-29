package com.suvenyra.wordcounterapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PremiumScreen(premiumManager: PremiumManager) {
    var code by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Įveskite Premium kodą", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = code,
            onValueChange = { code = it },
            label = { Text("Premium kodas") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (premiumManager.redeemCode(code)) {
                    message = "✅ Premium sėkmingai aktyvuotas!"
                } else {
                    message = "❌ Neteisingas kodas"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Aktyvuoti")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (message.isNotEmpty()) {
            Text(message)
        }
    }
}
