package lt.wordcounter.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import lt.wordcounter.app.ui.EmotionsScreen
import lt.wordcounter.app.ui.StatisticsScreen
import lt.wordcounter.app.ui.theme.WordCounterAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WordCounterAppTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "emotions") {
        composable("emotions") {
            EmotionsScreen(
                onShowStatistics = { navController.navigate("statistics") }
            )
        }
        composable("statistics") {
            StatisticsScreen(onBack = { navController.popBackStack() })
        }
    }
}
