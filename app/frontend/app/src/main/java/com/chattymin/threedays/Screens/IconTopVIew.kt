package com.chattymin.threedays.Screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.chattymin.threedays.R
import com.chattymin.threedays.ui.theme.Green
import com.chattymin.threedays.ui.theme.LightGreen

@Composable
fun IconTopView(isSetting: Boolean = false, padding: Int = 24, onClick: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier
            .padding(horizontal = padding.dp, vertical = 24.dp)
            .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Image(
            modifier = Modifier
                .size(60.dp),
            painter = painterResource(id = R.drawable.icon_small),
            contentDescription = "small icon"
        )

        if (isSetting) {
            Column() {
                Image(
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            expanded = !expanded
                        },
                    painter = painterResource(id = R.drawable.setting),
                    contentDescription = "setting icon"
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                ) {
                    DropdownMenuItem(
                        onClick = {
                            onClick()
                            expanded = false
                        }) {
                        Text(text = "로그아웃")
                    }
                }
            }
        } else
            Spacer(modifier = Modifier.size(24.dp))
    }
}