package com.chattymin.threedays.Frame

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.chattymin.threedays.ui.theme.Pink

@Composable
fun DayBoolean(text: String, id: Int){
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            modifier = Modifier
                .size(24.dp),
            painter = painterResource(id = id),
            contentDescription = "send",
            tint = Pink
        )
        Text(text = text, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
    }
}