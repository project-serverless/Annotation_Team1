package com.chattymin.threedays.Frame

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chattymin.threedays.ui.theme.LightGreen

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(text:String, searchText: MutableState<String>, maxWidth: Float = 0.6f, imeAction: ImeAction, onSearch: (String) -> Unit) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier
            .fillMaxWidth(maxWidth)
            .height(60.dp)
            .padding(8.dp)
            .border(
                BorderStroke(1.dp, Color.Black),
                shape = RoundedCornerShape(12.dp)
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = text + " : ", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        BasicTextField(
            value = searchText.value,
            onValueChange = { newText ->
                searchText.value = newText
            },
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, end = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
            visualTransformation = if (text != "PW") VisualTransformation.None else PasswordVisualTransformation(),
            keyboardActions = KeyboardActions(
                onNext = {
                    onSearch(searchText.value)
                    focusManager.moveFocus(FocusDirection.Down)
                    //keyboardController?.hide()
                },
                onDone = {
                    //onSearch(searchText)
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            )
        )
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchBar2(searchText: MutableState<String>, maxWidth: Float = 1f, onSearch: (String) -> Unit) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    Row(
        modifier = Modifier
            .fillMaxWidth(maxWidth)
            .height(60.dp)
            .padding(top = 12.dp, bottom = 8.dp, start = 8.dp, end = 8.dp)
            .clip(
                RoundedCornerShape(12.dp)
            )
            .background(LightGreen),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(modifier = Modifier.width(10.dp))
        Text(text = "목표 : ", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        BasicTextField(
            value = searchText.value,
            onValueChange = { newText ->
                searchText.value = newText
            },
            textStyle = TextStyle(color = Color.Black, fontSize = 16.sp),
            singleLine = true,
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp, end = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(
                onDone = {
                    //onSearch(searchText)
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
            )
        )
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun SearchBarPreview() {
    Box(modifier = Modifier.fillMaxSize()){
        SearchBar2(searchText = mutableStateOf(""), onSearch = {})
    }
}