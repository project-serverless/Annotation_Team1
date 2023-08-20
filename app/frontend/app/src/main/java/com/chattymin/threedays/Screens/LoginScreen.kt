package com.chattymin.threedays.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.chattymin.threedays.Frame.RoundCornerFrame
import com.chattymin.threedays.Frame.SearchBar
import com.chattymin.threedays.R
import com.chattymin.threedays.ui.theme.Green
import com.chattymin.threedays.ui.theme.LightGreen

@Composable
fun LoginScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGreen),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ){
        Image(modifier = Modifier.fillMaxSize(0.6f),painter = painterResource(id = R.drawable.icon_big), contentDescription = "icon")

        Column(horizontalAlignment = Alignment.CenterHorizontally,) {
            SearchBar(text = "ID", imeAction = ImeAction.Next,onSearch = {})
            SearchBar(text = "PW", imeAction = ImeAction.Done,onSearch = {})
            Text(
                modifier = Modifier.clickable {
                                              // 회원가입 페이지 이동
                },
                textDecoration = TextDecoration.Underline,
                text = "SignUp",
                fontSize = 12.sp
            )

            RoundCornerFrame(
                modifier = Modifier.clickable {
                                              // 로그인
                },
                borderColor = Green,
                arrangement = Arrangement.Center
            ){
                Text(text = "Login", color = Green)
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview(){
    LoginScreen()
}