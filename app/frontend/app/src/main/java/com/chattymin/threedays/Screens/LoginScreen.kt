package com.chattymin.threedays.Screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.chattymin.threedays.App
import com.chattymin.threedays.Frame.RoundCornerFrame
import com.chattymin.threedays.Frame.SearchBar
import com.chattymin.threedays.Navigation.MainScreenView
import com.chattymin.threedays.R
import com.chattymin.threedays.Retrofit.RetrofitManager
import com.chattymin.threedays.Utils.MESSAGE
import com.chattymin.threedays.Utils.RESPONSE_STATE
import com.chattymin.threedays.navigation.BottomScreen
import com.chattymin.threedays.navigation.Screen
import com.chattymin.threedays.ui.theme.Green
import com.chattymin.threedays.ui.theme.LightGreen

@Composable
fun LoginScreen(navController: NavController){
    var ID = remember { mutableStateOf("") }
    var PW = remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGreen),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Image(
            modifier = Modifier.fillMaxSize(0.6f),
            painter = painterResource(id = R.drawable.icon_big),
            contentDescription = "icon"
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally,) {
            SearchBar(text = "ID", searchText = ID, imeAction = ImeAction.Next, onSearch = {})
            SearchBar(text = "PW", searchText = PW, imeAction = ImeAction.Done, onSearch = {})
            Text(
                modifier = Modifier.clickable {
                    navController.navigate(Screen.Sign.route)
                    // 회원가입 페이지 이동
                },
                textDecoration = TextDecoration.Underline,
                text = "SignUp",
                fontSize = 12.sp
            )

            RoundCornerFrame(
                modifier = Modifier.clickable {
                    if (ID.value.isNotEmpty() && PW.value.isNotEmpty()){
                        RetrofitManager.instance.login(
                            ID = ID.value,
                            PW = PW.value,
                            completion = { responseState ->
                                when (responseState) {
                                    RESPONSE_STATE.OKAY -> {
                                        navController.navigate(Screen.Once.route)
                                    }
                                    RESPONSE_STATE.FAIL -> {
                                        Toast.makeText(App.instance, MESSAGE.ERROR, Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                }
                            })
                    }

                    navController.navigate(Screen.Once.route)
                },
                borderColor = Green,
                arrangement = Arrangement.Center
            ) {
                Text(text = "Login", color = Green)
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview(){
    //LoginScreen()
}