package com.chattymin.threedays.Frame

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chattymin.threedays.ui.theme.LightGreen
import com.chattymin.threedays.ui.theme.LightYellow

@Composable
fun RoundCornerFrame(modifier: Modifier = Modifier, maxWidth: Float = 0.6f,borderColor: Color, arrangement: Arrangement.Horizontal, item: @Composable RowScope.() -> Unit){
    Row(
        modifier = modifier
            .fillMaxWidth(maxWidth)
            .height(60.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .border(
                BorderStroke(1.dp, borderColor),
                shape = RoundedCornerShape(12.dp)
            ).background(LightYellow),
        horizontalArrangement = arrangement,
        verticalAlignment = Alignment.CenterVertically
    ) {
        item()
    }
}

@Preview
@Composable
fun RoundCornerFramePreview(){
    Box(modifier = Modifier.fillMaxSize()){
        RoundCornerFrame(modifier = Modifier, borderColor = LightGreen,arrangement = Arrangement.Center){
            Text(text = "Preview")
        }
    }
}