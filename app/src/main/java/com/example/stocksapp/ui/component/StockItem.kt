package com.example.stocksapp.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Loop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.colorspace.ColorSpace
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stocksapp.ui.screens.HomeScreen
import com.example.stocksapp.ui.theme.StocksAppTheme

@Composable
fun StockItem (modifier: Modifier){
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column (modifier = Modifier.fillMaxSize()){
            Icon(
                Icons.Rounded.Loop, contentDescription = "Logo",
                Modifier
                    .scale(1f)
                    .align(Alignment.Start)
                    .padding(top = 7.dp, start = 7.dp)
                    .clip(CircleShape)
            )

            Text(
                text = "Google", fontSize = 20.sp, modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = "20$", fontSize = 15.sp, modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemPreview() {
    StocksAppTheme {
        StockItem(modifier = Modifier.width(130.dp).wrapContentHeight())
    }
}