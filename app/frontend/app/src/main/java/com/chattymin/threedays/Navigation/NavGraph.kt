package com.chattymin.threedays.Navigation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.chattymin.threedays.Screens.*
import com.chattymin.threedays.navigation.*
import com.chattymin.threedays.ui.theme.Green
import com.chattymin.threedays.ui.theme.LightGreen
import java.lang.Exception

@Composable
fun SetupNavGraph(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(route = Screen.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.Sign.route) {
            SignUpScreen(navController = navController)
        }
        composable(route = Screen.Once.route) {
            OnceScreen(BottomScreen.Main.screenRoute)
        }
    }
}

@Composable
fun BottomNavigation(navController: NavHostController) {
    val items = listOf(
        BottomScreen.Main,
        BottomScreen.Friend
    )

    BottomNavigation(backgroundColor = LightGreen, elevation = 10.dp) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            BottomNavigationItem(
                icon = {
                    Icon(
                        imageVector = if (currentRoute?.startsWith(item.screenRoute) == true) item.iconSolid else item.iconOutline,
                        contentDescription = item.title,
                        modifier = Modifier
                            .size(32.dp)
                    )
                },
                label = { Text(item.title, fontSize = 11.sp) },
                selectedContentColor = Green,
                unselectedContentColor = Color.LightGray,
                selected = currentRoute?.startsWith(item.screenRoute) == true,
                alwaysShowLabel = false,
                onClick = {
                    try {
                        navController.navigate(item.screenRoute) {
                            if (currentRoute?.startsWith(item.screenRoute) != true) {
                                navController.graph.startDestinationRoute?.let {
                                    popUpTo(it) { saveState = true }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    } catch (e: Exception) {
                        navController.navigate(item.screenRoute)
                    }
                }
            )
        }
    }
}

@Composable
fun MainScreenView(startDestination: String) {
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomNavigation(navController = navController)
        }
    ) {
        Box(Modifier.padding(it)) {
            NavHost(
                navController = navController,
                startDestination = startDestination,
            ) {
                // Main
                composable(MainNavigationScreens.Main.route) {
                    MainScreen(navController = navController)
                }

                // Friend
                composable(FriendNavigationScreens.FriendList.route) {
                    FriendListScreen(navController = navController)
                }
                composable(
                    route = "${FriendNavigationScreens.FriendDetail.route}/{variable}",
                    arguments = listOf(navArgument("variable") { type = NavType.StringType })
                ) { entry ->
                    val variable = entry.arguments?.getString("variable")
                    FriendDetailScreen(
                        navController = navController,
                        userId = variable!!.toString()
                    )
                }

//                composable(FriendNavigationScreens.FriendDetail.route) {
//                    FriendDetailScreen(navController = navController, "")
//                }
            }
        }
    }
}