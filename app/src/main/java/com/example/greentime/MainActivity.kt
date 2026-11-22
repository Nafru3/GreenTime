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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.greentime.ui.theme.GreenTimeTheme
import com.example.greentime.views.home.HomeScreen
import com.example.greentime.views.permission.PermissionView
import com.example.greentime.views.permission.PermissionViewModel
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
    val context = LocalContext.current
    val permissionViewModel = remember { PermissionViewModel() }
    // État mémorisé pour savoir si l'accès aux stats d'utilisation est autorisé
    var isAuthorized by remember { mutableStateOf(permissionViewModel.hasUsagePermission(context)) }

    val lifecycleOwner = LocalLifecycleOwner.current
    
    // Observer le cycle de vie pour revérifier la permission en temps réel 
    // (ex: lorsque l'utilisateur revient des paramètres système après avoir activé l'accès)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                // Dès que l'application redevient active, on met à jour l'état de la permission
                isAuthorized = permissionViewModel.hasUsagePermission(context)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            // Nettoyage de l'observateur lorsque le composable est détruit
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Si la permission n'est pas accordée, on affiche UNIQUEMENT l'écran de permission (sans Scaffold ni Navbar)
    if (!isAuthorized) {
        PermissionView()
        return
    }

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
        composable(Destinations.Home.route) { HomeScreen() }
        composable(Destinations.Stats.route) { StatScreen() }
        composable(Destinations.Social.route) { Text("Social") }
    }
}
