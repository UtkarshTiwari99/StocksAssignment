package com.example.stocksapp.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.TurnSlightRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stocksapp.ui.theme.StocksAppTheme

@Composable
fun StockItem (modifier: Modifier){
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        border = BorderStroke(0.1.dp, Color.LightGray),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column (modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 11.dp, horizontal = 8.dp)){
            Image(bitmap = ImageBitmap(300,300),modifier=Modifier.background(Color.Cyan), contentDescription = "")
            Text(text = "Google", fontSize = 20.sp, modifier = Modifier.padding(top=8.dp, bottom = 9.dp))
            Text(
                text = "20$", fontSize = 20.sp, modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
            )
            Row {
                Text(text = "+0.55(55%)", fontSize = 13.sp)
                Icon(
                    Icons.Rounded.TurnSlightRight, contentDescription = "Logo",
                    Modifier
                        .scale(0.8f)
                        .padding(start = 6.dp)
                        .clip(CircleShape)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemPreview() {
    StocksAppTheme {
        StockItem(modifier = Modifier
            .wrapContentSize())
    }
}