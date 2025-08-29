package lt.wordcounter.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import lt.wordcounter.app.ui.theme.WordCounterAppTheme
import lt.wordcounter.app.ui.MainScreen
import lt.wordcounter.app.ui.StatisticsScreen
import lt.wordcounter.app.ui.PremiumScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordCounterAppTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main"
    ) {
        composable("main") {
            MainScreen(
                onNavigateToStatistics = { navController.navigate("statistics") },
                onNavigateToPremium = { navController.navigate("premium") }
            )
        }
        composable("statistics") {
            StatisticsScreen(onBack = { navController.popBackStack() })
        }
        composable("premium") {
            PremiumScreen(onBack = { navController.popBackStack() })
        }
    }
}
