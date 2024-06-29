package com.example.stocksapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.TurnSlightRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stocksapp.ui.theme.StocksAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(modifier: Modifier){
    Column (modifier=modifier.fillMaxSize()){

        Box (modifier= Modifier
            .fillMaxWidth()
            .height(LocalConfiguration.current.screenHeightDp.times(0.1f).dp)
            .padding(start = 10.dp, end = 10.dp, top = 10.dp))
        {
            Row(modifier = Modifier.align(Alignment.CenterStart)) {
                Image(
                    bitmap = ImageBitmap(300, 300), modifier = Modifier
                        .padding(3.dp)
                        .background(Color.Cyan), contentDescription = ""
                )
                Column(modifier = Modifier
                    .padding(top = 3.dp)
                    .align(Alignment.CenterVertically)) {
                    Text("Apple NIC", fontSize = 18.sp, fontWeight = FontWeight(700))
                    Text(
                        text = "Apple, Common Stock",
                        fontSize = 12.sp,
                        fontWeight = FontWeight(600)
                    )
                    Text(text = "NFC", fontSize = 12.sp, fontWeight = FontWeight(600))
                }
            }
            Column (modifier = Modifier.align(Alignment.CenterEnd), horizontalAlignment = Alignment.End)
            {
                Text(text = "+0.55", fontSize = 13.sp, style = TextStyle(
                    platformStyle = PlatformTextStyle(
                        includeFontPadding = false
                    ), fontSize = 10.sp), modifier = Modifier.padding(end=6.dp))
                Row {
                    Text(text = "55%", fontSize = 13.sp,style = TextStyle(
                        platformStyle = PlatformTextStyle(
                            includeFontPadding = false
                        ), fontSize = 10.sp))
                    Icon(
                        Icons.Rounded.TurnSlightRight, contentDescription = "Logo",
                        Modifier
                            .scale(0.8f)
                            .clip(CircleShape)
                    )
                }
            }
        }

        Column (modifier= modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())){

        Card(
            Modifier
                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                .fillMaxHeight(0.5f), shape = RoundedCornerShape(10.dp), border = BorderStroke(1.dp,
            Color.Gray.copy(0.7f)))
        {
            Column(modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(vertical = 6.dp)){
                PerformanceChart(Modifier.height(LocalConfiguration.current.screenHeightDp.times(0.4f).dp))
                Row(
                    modifier = Modifier
                        .wrapContentSize()
                        .align(Alignment.CenterHorizontally)
                        .border(
                            1.dp, Color.Gray.copy(0.3f),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                )
                {
                    val list = listOf("1D", "1W", "1M", "3M", "6M", "1Y");
                    list.forEachIndexed{index, it ->
                        Button(shape = RoundedCornerShape(18.dp),
                            modifier = Modifier
                                .padding(2.dp)
                                .width(23.dp)
                                .aspectRatio(1f), border = BorderStroke(1.dp, if(index==0) Color.Gray.copy(0.6f) else Color.White),
                            enabled = index==0,
                            colors = ButtonDefaults.buttonColors().copy(
                                containerColor = Color.Red.copy(0.6f),
                                contentColor = Color.White,
                                disabledContainerColor = Color.White,
                                disabledContentColor = Color.Black
                            ),
                            contentPadding = PaddingValues(0.dp),
                            onClick = { /*TODO*/ }) {
                            Text(text = it)
                        }
                    }
                }
            }
        }

        Card(
            Modifier
                .padding(10.dp)
                .fillMaxHeight(), shape = RoundedCornerShape(6.dp), border = BorderStroke(
                1.dp,
                Color.Gray.copy(0.7f)
            )
        )
        {
            Column(modifier= Modifier
                .fillMaxSize()
                .background(Color.White)) {
                Text(text = "About Apple INC", fontSize = 13.sp, color = Color.Black, fontWeight = FontWeight(700), modifier=Modifier.padding(6.dp))
                HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 2.dp)
                Column(modifier = Modifier.padding(6.dp)) {
                    Text(modifier = Modifier.padding(bottom = 10.dp),
                        text = "Points to keep in check:\n" +
                                "● You’ll need an API key in order to access the endpoints, generate an API\n" +
                                "key for the app. Check limit on request per minute and request per day."
                    )
                    Row (modifier= Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .align(Alignment.CenterHorizontally))
                    {
                        Text(
                            "Industry: Electronic Computers",
                            fontSize = 14.sp,
                            color = Color.Red.copy(0.7f), style = TextStyle(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                ), fontSize = 10.sp),
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .background(Color.Red.copy(0.16f), RoundedCornerShape(16.dp))
                                .padding(vertical = 5.dp, horizontal = 9.dp)
                        )
                        Text(text = "Sector: Technology", style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            ), fontSize = 14.sp),
                                fontSize = 13.sp,
                            color = Color.Red.copy(0.7f),
                            modifier = Modifier
                                .background(Color.Red.copy(0.16f), RoundedCornerShape(16.dp))
                                .padding(vertical = 5.dp, horizontal = 8.dp)
                        )
                    }

                    Row(modifier = Modifier
                        .padding()
                        .fillMaxWidth())
                    {
                        MetricVisual(Alignment.Start, metricName = "52-Week Low", value = "523")
                        Slider(
                            modifier = Modifier
                                .fillMaxWidth(0.66f)
                                .align(Alignment.Bottom),
                            value = 406f,
                            colors = SliderDefaults.colors().copy(
                                disabledInactiveTickColor = Color.Gray.copy(0.3f),
                                activeTrackColor = Color.Gray.copy(0.3f),
                                inactiveTrackColor = Color.Gray.copy(0.3f),
                                disabledActiveTrackColor = Color.Gray.copy(0.3f),
                                inactiveTickColor = Color.Gray.copy(0.3f),
                                disabledThumbColor = Color.Gray,
                                activeTickColor = Color.Gray.copy(0.3f),
                                thumbColor = Color.Gray,
                                disabledActiveTickColor = Color.Gray.copy(0.3f),
                                disabledInactiveTrackColor = Color.Gray.copy(0.3f)
                            ),
                            onValueChange = {},
                            enabled = false,
                            valueRange = 0f..532f,
                            thumb = {
                                Column(modifier=Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("456", style = TextStyle(
                                        platformStyle = PlatformTextStyle(
                                            includeFontPadding = false
                                        ), fontSize = 10.sp))
                                    Icon(
                                        Icons.Rounded.ArrowDropDown, contentDescription = "Logo",
                                        Modifier
                                            .scale(1f)
                                            .clip(CircleShape)
                                    )
                                }
                            }
                        )
                        MetricVisual(Alignment.End, metricName = "52-Week High", value = "523")
                    }
                    Row(modifier=Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        val list= listOf(Pair("Market Cap","$2.77T"),Pair("P/E Ratio","27.77"),Pair("Beta","1.308"),Pair("Dividend Yield","0.54%"),Pair("Profit Margin","0.247"));
                        list.forEach {
                            MetricVisual(alignment = Alignment.Start, metricName = it.first, value = it.second)
                        }
                    }
                }
            }
        }
            }
    }
}

@Composable
private fun MetricVisual(alignment: Alignment.Horizontal, metricName:String, value:String,){
    Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp), horizontalAlignment = alignment, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = metricName,
            textAlign = if(alignment==Alignment.Start) TextAlign.Start else TextAlign.End,
            fontSize = 13.sp,
            color = Color.Black.copy(0.3f), style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            ),
            fontWeight = FontWeight(600)
        )
        Text(
            textAlign = if(alignment==Alignment.Start) TextAlign.Start else TextAlign.End,
            text = value,
            fontSize = 15.sp,
            style = TextStyle(
                platformStyle = PlatformTextStyle(
                    includeFontPadding = false
                )
            ),
            color = Color.Black,
            fontWeight = FontWeight(700)
        )
    }
}

private fun getValuePercentageForRange(value: Float, max: Float, min: Float) =
    (value - min) / (max - min)

@Composable
fun PerformanceChart(modifier: Modifier = Modifier, list: List<Float> = listOf(10f, 20f, 3f, 1f,10f,20f,30f,10f,22f,22.4f,22f,10f,20f,30f,10f,22f,22.4f,22f,10f,20f,30f,10f,22f,22.4f,22f,10f,20f,30f,10f,22f,22.4f,22f,10f,20f,30f,10f,22f,22.4f,22f)) {
    val zipList: List<Pair<Float, Float>> = list.zipWithNext()

    Row(modifier = modifier.padding(6.dp)) {

        var isVisual by remember {
            mutableFloatStateOf(0f)
        }

        Text(text = "$isVisual", fontSize = 20.sp, color = Color.Magenta)

        val max = list.max()
        val min = list.min()

        val lineColor =
            if (list.last() > list.first()) Color.Black else Color.Black // <-- Line color is Green if its going up and Red otherwise

        for (pair in zipList) {

            val fromValuePercentage = getValuePercentageForRange(pair.first, max, min)
            val toValuePercentage = getValuePercentageForRange(pair.second, max, min)

            Canvas(
                modifier = Modifier
                    .background(Color.White)
                    .pointerInput(key1 = "someStringKey?") {
                        detectTapGestures(
                            onPress = { isVisual = pair.first }
                        )
                    }
                    .fillMaxHeight()
                    .weight(1f),
                onDraw = {
                    val fromPoint = Offset(
                        x = 0f,
                        y = size.height.times(1 - fromValuePercentage)
                    ) // <-- Use times so it works for any available space
                    val toPoint =
                        Offset(
                            x = size.width,
                            y = size.height.times(1 - toValuePercentage)
                        ) // <-- Also here!
                    drawLine(
                        color = lineColor,
                        start = fromPoint,
                        end = toPoint,
                        strokeWidth = 3f
                    )
                })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    StocksAppTheme {
        DetailScreen(
            Modifier
                .padding(0.dp)
                .fillMaxSize())
    }
}