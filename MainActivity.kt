package lt.wordcounter.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import lt.wordcounter.app.ui.theme.WordCounterAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordCounterAppTheme {
                val navController = rememberNavController()
                val premiumManager = remember { PremiumManager(this) }

                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavHost(navController, premiumManager)
                }
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController, premiumManager: PremiumManager) {
    NavHost(navController = navController, startDestination = "home") {
        // Pagrindinis ekranas
        composable("home") {
            HomeScreen(navController, premiumManager)
        }

        // Premium ekranas
        composable("premium") {
            PremiumScreen(premiumManager)
        }
    }
}

@Composable
fun HomeScreen(navController: NavHostController, premiumManager: PremiumManager) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Word Counter App") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).padding(16.dp)
        ) {
            Text("Sveikas! Čia pagrindinis ekranas 😊")

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.navigate("premium") }) {
                Text("Premium")
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (premiumManager.isPremium()) {
                Text("✅ Jūs turite Premium planą!")
            } else {
                Text("🚀 Atrakinkite Premium planą!")
            }
        }
    }
}
