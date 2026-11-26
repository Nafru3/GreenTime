package com.example.greentime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.greentime.ui.theme.GreenTimeTheme
import com.example.greentime.views.AppList.AppListScreen
import com.example.greentime.views.stats.StatScreen
import com.example.test.BarChartFilled
import com.example.test.BarChartOutlined
import com.example.test.GroupFilled
import com.example.test.GroupOutlined
import com.example.test.HomeFilled
import com.example.test.HomeOutlined
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GreenTimeTheme {
                GreenTimeApp()
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 412, heightDp = 915)
@Composable
fun GreetingPreview() {
    GreenTimeTheme {
        GreenTimeApp()
    }
}

enum class Destinations(
    val route: String,
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val contentDescription: String
) {
    Home(
        route = "home",
        label = "Accueil",
        icon = HomeOutlined,
        selectedIcon = HomeFilled,
        contentDescription = "Accueil"
    ),
    Stats(
        route = "stats",
        label = "Stats",
        icon = BarChartOutlined,
        selectedIcon = BarChartFilled,
        contentDescription = "Statistiques"
    ),
    Social(
        route = "social",
        label = "Social",
        icon = GroupOutlined,
        selectedIcon = GroupFilled,
        contentDescription = "Social"
    )
}

@Composable
fun GreenTimeApp() {
    val navController = rememberNavController()
    val startDestination = Destinations.Home

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier,
        bottomBar = {
            NavigationBar(windowInsets = NavigationBarDefaults.windowInsets) {
                Destinations.entries.forEachIndexed { index, destination ->
                    val selected = currentRoute == destination.route

                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(destination.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (selected) destination.selectedIcon else destination.icon,
                                contentDescription = destination.contentDescription
                            )
                        },
                        label = { Text(destination.label) }
                    )
                }
            }
        }
    ) { contentPadding ->
        AppNavHost(navController, startDestination, modifier = Modifier.padding(contentPadding))
    }
}

@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: Destinations,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = modifier
    ) {
        composable(Destinations.Home.route) { AppListScreen() }
        composable(Destinations.Stats.route) { StatScreen() }
        composable(Destinations.Social.route) { Text("Social") }
    }
}