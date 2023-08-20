package com.chattymin.threedays.Screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chattymin.threedays.Frame.RoundCornerFrame
import com.chattymin.threedays.Frame.SearchBar
import com.chattymin.threedays.R
import com.chattymin.threedays.ui.theme.Green
import com.chattymin.threedays.ui.theme.LightGreen

@Composable
fun SignUpScreen(){
    var popUp by remember{
        mutableStateOf(false)
    }
    var popUp2 by remember{
        mutableStateOf(false)
    }

    if (popUp) {
        //val SearchAddressInput = remember { mutableStateOf(TextFieldValue()) }
        AlertDialog(
            onDismissRequest = {
                popUp = false
            },
            text = {
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                        Column(modifier = Modifier
                            //.fillMaxWidth()
                            .padding(start = 12.dp, bottom = 12.dp, end = 12.dp), horizontalAlignment = Alignment.Start) {
                            Text("이메일로 인증번호를 보냈습니다!", fontWeight = FontWeight.Bold)
                            Text("인증번호를 입력해주세요 :)", fontWeight = FontWeight.Bold)
                        }
                        Icon(
                            modifier = Modifier
                                .size(24.dp)
                                .clickable {
                                    popUp = false
                                },
                            painter = painterResource(id = R.drawable.x),
                            contentDescription = "popDown",
                            tint = Color.Black
                        )
                    }


                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                        SearchBar(text = "인증번호", maxWidth = 0.9f, imeAction = ImeAction.Done,onSearch = {})
                        Icon(
                            modifier = Modifier
                                .size(40.dp)
                                .clickable {
                                    popUp = false
                                    popUp2 = true
                                },
                            painter = painterResource(id = R.drawable.send_fill),
                            contentDescription = "send",
                            tint = Green
                        )
                    }
                }

            },
            confirmButton = {},
            shape = RoundedCornerShape(12.dp),
            backgroundColor = LightGreen
        )
    }

    if (popUp2){
        AlertDialog(
            onDismissRequest = {
                popUp2 = false
            },
            confirmButton = {
                Column() {
                    Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.Start) {
                        Text("이메일로 인증번호 완료!")
                        Text("작심3일을 마음껏 즐겨주세요 :)")
                    }

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        RoundCornerFrame(
                            modifier = Modifier
                                .clickable {
                                    // 로그인창으로 이동
                                    popUp2 = false
                                },
                            maxWidth = 0.5f,
                            borderColor = Green,
                            arrangement = Arrangement.Center
                        ){
                            Text(text = "확인", color = Green)
                        }
                    }
                }

            },
            shape = RoundedCornerShape(12.dp),
            backgroundColor = LightGreen
        )
    }



    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = LightGreen),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        IconTopView(false)
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            SearchBar(text = "ID", maxWidth = 1f, imeAction = ImeAction.Next,onSearch = {})
            SearchBar(text = "PW", maxWidth = 1f, imeAction = ImeAction.Next,onSearch = {})
            SearchBar(text = "E-Mail", maxWidth = 1f,imeAction = ImeAction.Next,onSearch = {})
            SearchBar(text = "Nick Name", maxWidth = 1f, imeAction = ImeAction.Next,onSearch = {})
            SearchBar(text = "How Are You?", maxWidth = 1f, imeAction = ImeAction.Done,onSearch = {})
            RoundCornerFrame(
                modifier = Modifier
                    .clickable {
                    // api 호출 and 팝업
                               popUp = true
                },
                maxWidth = 0.8f,
                borderColor = Green,
                arrangement = Arrangement.Center
            ){
                Text(text = "Let's Make Woderful 3 Days!", color = Green)
            }
            Spacer(modifier = Modifier.size(24.dp))
        }
    }
}

@Preview
@Composable
fun SignUpScreenPreview(){
    SignUpScreen()
}

@Preview
@Composable
fun PopUpPreview(){
    AlertDialog(
        onDismissRequest = {
            //popUp = false
        },
        text = {
            Column {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp), horizontalAlignment = Alignment.Start) {
                    Text("이메일로 인증번호를 보냈습니다!", fontWeight = FontWeight.Bold)
                    Text("인증번호를 입력해주세요 :)", fontWeight = FontWeight.Bold)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                    SearchBar(text = "인증번호", maxWidth = 0.9f, imeAction = ImeAction.Done,onSearch = {})
                    Icon(
                        modifier = Modifier
                            .size(40.dp),
                        painter = painterResource(id = R.drawable.send_fill),
                        contentDescription = "send",
                        tint = Green
                    )
                }
            }

        },
        confirmButton = {},
        shape = RoundedCornerShape(12.dp),
        backgroundColor = LightGreen
    )
}