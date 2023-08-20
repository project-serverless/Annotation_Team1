package com.chattymin.threedays.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.chattymin.threedays.R

@Composable
fun IconTopView(isSetting: Boolean = false) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Image(
            modifier = Modifier
                .padding(24.dp)
                .size(60.dp),
            painter = painterResource(id = R.drawable.icon_small),
            contentDescription = "small icon"
        )

        if (isSetting){
            Image(
                modifier = Modifier
                    .padding(24.dp)
                    .size(24.dp)
                    .clickable {  },
                painter = painterResource(id = R.drawable.setting),
                contentDescription = "setting icon"
            )
        }else
            Spacer(modifier = Modifier.size(24.dp))
    }
}