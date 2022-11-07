/*
 * Copyright 2021 Spikey Sanju
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.epoch.components

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.stop.watch.IpAppPurchaseActivity
import com.stop.watch.R
import com.stop.watch.ui.theme.whiteText

@Composable
fun TopBar() {

    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Stop Watch",
                color= whiteText,
                style = MaterialTheme.typography.h4
            )

            Icon(
                painter = painterResource(id = R.drawable.premium),
                contentDescription = null,
                tint    = androidx.compose.ui.graphics.Color.White,
                modifier = Modifier
                    .size(20.dp)
                    .clickable {
                        openPaymentActivity(context)

                    }

            )

        }

    }

}

fun openPaymentActivity(context: Context) {

    context.startActivity(Intent(context, IpAppPurchaseActivity::class.java))

}

@Composable
fun Toolbar() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        TopBar()
    }
}
