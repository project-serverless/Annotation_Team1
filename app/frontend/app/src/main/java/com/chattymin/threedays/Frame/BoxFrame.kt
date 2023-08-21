package com.chattymin.threedays.Frame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.chattymin.threedays.ui.theme.Green
import com.chattymin.threedays.ui.theme.LightYellow

@Composable
fun BoxFrame(modifier: Modifier = Modifier, size: Float = 1f, color: Color = Green, item: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = modifier
            .fillMaxWidth(size)
            .clip(shape = RoundedCornerShape(12.dp))
            .background(color),
    ) {
        item()
    }
}

@Composable
fun SmallBoxFrame(modifier: Modifier = Modifier, size: Int, item: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .background(LightYellow),
        contentAlignment = Alignment.Center
    ) {
        item()
    }
}