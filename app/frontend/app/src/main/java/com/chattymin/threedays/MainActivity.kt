package com.chattymin.threedays

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.chattymin.threedays.Navigation.SetupNavGraph
//import com.chattymin.threedays.Navigation.SetupNavGraph
//import com.chattymin.threedays.Navigation.MainScreenView
import com.chattymin.threedays.Screens.FriendDetailScreen
import com.chattymin.threedays.Screens.LoginScreen
import com.chattymin.threedays.Screens.MainScreen
import com.chattymin.threedays.Screens.SignUpScreen
import com.chattymin.threedays.ui.theme.LightGreen
import com.chattymin.threedays.ui.theme.ThreedaysTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ThreedaysTheme {
                Box(
                    modifier = Modifier.fillMaxSize().background(LightGreen),
                ){
                    installSplashScreen()

                }
                val navController = rememberNavController()
                SetupNavGraph(navController = navController)
                    //LoginScreen(navController = navController)
                    //SignUpScreen(navController = navController)
                    //MainScreen(navController = rememberNavController())
                    //MainScreenView("main")
                    //FriendDetailScreen()

            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ThreedaysTheme {
        Greeting("Android")
    }
}