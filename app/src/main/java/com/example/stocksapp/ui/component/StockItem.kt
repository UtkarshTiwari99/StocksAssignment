package com.example.stocksapp.ui.component

import android.icu.number.NumberFormatter
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material.icons.rounded.Balance
import androidx.compose.material.icons.rounded.TurnSlightRight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.stocksapp.data.dto.Stock
import com.example.stocksapp.ui.theme.StocksAppTheme
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Formatter
import java.util.function.DoubleFunction

fun genrateImage():String {
    val links =
        listOf("https://logos-world.net/wp-content/uploads/2020/09/Oracle-Emblem.jpg","https://th.bing.com/th/id/OIP.UcklnyxiP2gyRdTxtVAhvQHaHa?rs=1&pid=ImgDetMain","https://th.bing.com/th/id/OIP.cCkt-lMjHsMIqfVx8xeg9gHaKe?rs=1&pid=ImgDetMain","https://1000logos.net/wp-content/uploads/2016/11/meta-logo-1536x864.png","https://cdn.freelogovectors.net/wp-content/uploads/2023/11/groww_logo-freelogovectors.net_.png","https://th.bing.com/th/id/OIP.Bs2PlI-wmfFiYzgWNRO0pgHaDO?rs=1&pid=ImgDetMain","https://i.pinimg.com/originals/01/ca/da/01cada77a0a7d326d85b7969fe26a728.jpg","","https://th.bing.com/th/id/R.7e557f1c0864829c54c300d15bee69f4?rik=fjZN1AYH30vXIw&riu=http%3a%2f%2fpngimg.com%2fuploads%2fgoogle%2fgoogle_PNG19635.png&ehk=ZmsumEtoeJQhKoUzQTZO2TEbYPBu0%2b7EFdjmJ3qljls%3d&risl=&pid=ImgRaw&r=0")
    return links.random()
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun StockItem (modifier: Modifier,stock: Stock){
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.1.dp, Color.LightGray),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        val formatter = remember {
            DecimalFormat("#,###.##")
        }

        Column (modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 11.dp, horizontal = 13.dp)){
            val painter =
                rememberImagePainter(genrateImage())

            Image(contentScale = ContentScale.Fit,painter = painter,modifier=Modifier.background(Color.White).size(130.dp), contentDescription = "")
            Text(text = stock.ticker, fontSize = 17.sp, fontWeight = FontWeight(500), modifier = Modifier.padding(top=8.dp, bottom = 9.dp))
            Text(
                text = "${formatter.format(stock.price)}$", fontSize = 16.sp,fontWeight = FontWeight(490) ,modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.Start)
            )
            Row {
                Text(
                    text = "${
                        if (stock.change_amount < 0) formatter.format(stock.change_amount) else "+${
                            formatter.format(
                                stock.change_amount
                            )
                        }"
                    }(${
                        formatter.format(
                            stock.change_percentage.removeSuffix("%").removePrefix("-").toDouble()
                        )
                    }%)", fontSize = 13.sp, color = if(stock.change_amount==0.0000000) Color.Blue else if(stock.change_amount<0) Color.Red else Color.Green
                )
                if(stock.change_amount==0.0000000){
                    Icon(
                        Icons.Rounded.Balance, contentDescription = "Equal",
                        Modifier
                            .scale(1.3f)
                            .offset(x = (-3).dp)
                            .clip(CircleShape), tint = Color.Blue
                    )
                }
                else if(stock.change_amount<0){
                Icon(
                    Icons.Rounded.ArrowDropDown, contentDescription = "Less",
                    Modifier
                        .scale(1.3f)
                        .offset(x = (-3).dp)
                        .clip(CircleShape), tint = Color.Red
                )}
                else{
                    Icon(
                        Icons.Rounded.ArrowDropUp, contentDescription = "More",
                        Modifier
                            .scale(1.3f)
                            .offset(x = (-3).dp)
                            .clip(CircleShape), tint = Color.Green
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItemPreview() {
    StocksAppTheme {
        StockItem(modifier = Modifier
            .wrapContentSize(),Stock("Google",20.0,0.55,"55%"))
    }
}