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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import com.example.stocksapp.data.local.StockData
import com.example.stocksapp.data.local.StockTimeSeriesItem
import com.example.stocksapp.data.viewmodel.StockViewModel
import com.example.stocksapp.ui.component.genrateImage
import com.example.stocksapp.ui.component.rememberMarker
import com.example.stocksapp.ui.theme.StocksAppTheme
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineSpec
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberFadingEdges
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberShapeComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.of
import com.patrykandpatrick.vico.compose.common.shader.color
import com.patrykandpatrick.vico.core.cartesian.DefaultPointConnector
import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.common.Dimensions
import com.patrykandpatrick.vico.core.common.shader.DynamicShader
import com.patrykandpatrick.vico.core.common.shape.Shape
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun DetailScreen(modifier: Modifier, stockViewModel: StockViewModel,stockInfo: StockData){

    val isGraphLoading by stockViewModel.isGraphLoading.collectAsState(true)

        val formatter = remember {
            DecimalFormat("#,###.##")
        }

        val lineGraphData = stockInfo.stocks

        Column(modifier = modifier.fillMaxSize()) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(LocalConfiguration.current.screenHeightDp.times(0.13f).dp)
                    .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 10.dp)
            )
            {
                Row(modifier = Modifier.align(Alignment.CenterStart)) {
                    val painter =
                        rememberImagePainter(genrateImage())

                    Image(contentScale = ContentScale.Fit,painter = painter,modifier= Modifier
                        .background(Color.White)
                        .size(80.dp), contentDescription = "")

                    Column(
                        modifier = Modifier
                            .padding(top = 3.dp)
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            stockInfo.symbol,
                            fontSize = 18.sp,
                            fontWeight = FontWeight(700)
                        )
                        Text(
                            text = "${stockInfo.symbol} ${stockInfo.assetType}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight(600)
                        )
                        Text(
                            text = stockInfo.exchange,
                            fontSize = 12.sp,
                            fontWeight = FontWeight(600)
                        )
                    }
                }
                Column(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    horizontalAlignment = Alignment.End
                )
                {
                    Text(
                        text = stockInfo.price.toString(), fontSize = 13.sp, style = TextStyle(
                            platformStyle = PlatformTextStyle(
                                includeFontPadding = false
                            ), fontSize = 10.sp,
                            color = if((stockInfo.change_amount)>0.000000) Color.Green else Color.Red
                        ), modifier = Modifier.padding(end = 6.dp)
                    )
                    Row(verticalAlignment = Alignment.Top) {
                        Text(
                            text = (stockInfo.change_percentage),
                            fontSize = 13.sp,
                            color = if((stockInfo.change_amount)>0.000000) Color.Green else Color.Red,
                            style = TextStyle(
                                platformStyle = PlatformTextStyle(
                                    includeFontPadding = false
                                ), fontSize = 10.sp
                            )
                        )

                       if(stockInfo.change_amount.toDouble()<0.0) {
                            Icon(
                                Icons.Rounded.ArrowDropDown, contentDescription = "Logo",
                                Modifier
                                    .clip(CircleShape)
                                    .offset(x = (-3).dp, y = (-3).dp)
                            )
                        }
                        else{
                            Icon(
                                Icons.Rounded.ArrowDropUp, contentDescription = "Logo",
                                Modifier
                                    .clip(CircleShape)
                                    .offset(x = (-3).dp, y = (-3).dp)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            )
            {

                Card(
                    Modifier
                        .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
                        .fillMaxHeight(0.5f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(
                        1.dp,
                        Color.Gray.copy(0.7f)
                    )
                )
                {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                            .padding(vertical = 6.dp)
                    ) {
                        val datapoint = mutableListOf<Float>()
                        for (a in 1..3) {
                            datapoint.add((1..1000).random().toFloat())
                        }
                        PerformanceChart(
                            list = lineGraphData,
                            modifier = Modifier.height(
                                LocalConfiguration.current.screenHeightDp.times(
                                    0.4f
                                ).dp
                            ), isLoading = isGraphLoading
                        )
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
                            val list = listOf("1D", "1W", "1M", "3M", "6M", "1Y")
                            list.forEachIndexed { index, it ->
                                Button(shape = RoundedCornerShape(18.dp),
                                    modifier = Modifier
                                        .padding(2.dp)
                                        .width(23.dp)
                                        .aspectRatio(1f),
                                    border = BorderStroke(
                                        1.dp,
                                        if (index == 0) Color.Gray.copy(0.6f) else Color.White
                                    ),
                                    enabled = index == 0,
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
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White)
                    ) {
                        Text(
                            text = "About Apple INC",
                            fontSize = 13.sp,
                            color = Color.Black,
                            fontWeight = FontWeight(700),
                            modifier = Modifier.padding(6.dp)
                        )
                        HorizontalDivider(modifier = Modifier.fillMaxWidth(), thickness = 2.dp)
                        Column(modifier = Modifier.padding(6.dp)) {
                            Text(
                                modifier = Modifier.padding(bottom = 10.dp),
                                text = stockInfo.description
                            )
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp)
                                    .align(Alignment.CenterHorizontally),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            )
                            {
                                Text(
                                    "Industry: ${stockInfo.industry}",
                                    fontSize = 14.sp,
                                    color = Color.Red.copy(0.7f), style = TextStyle(
                                        platformStyle = PlatformTextStyle(
                                            includeFontPadding = false
                                        ), fontSize = 10.sp
                                    ),
                                    modifier = Modifier
                                        .padding(end = 16.dp)
                                        .background(
                                            Color.Red.copy(0.16f),
                                            RoundedCornerShape(16.dp)
                                        )
                                        .padding(vertical = 5.dp, horizontal = 9.dp)
                                )
                                Text(
                                    text = "Sector: ${stockInfo.sector}", style = TextStyle(
                                        platformStyle = PlatformTextStyle(
                                            includeFontPadding = false
                                        ), fontSize = 14.sp
                                    ),
                                    fontSize = 13.sp,
                                    color = Color.Red.copy(0.7f),
                                    modifier = Modifier
                                        .background(
                                            Color.Red.copy(0.16f),
                                            RoundedCornerShape(16.dp)
                                        )
                                        .padding(vertical = 5.dp, horizontal = 8.dp)
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .padding()
                                    .fillMaxWidth()
                            )
                            {
                                MetricVisual(
                                    alignment = Alignment.Start,
                                    metricName = "52-Week Low",
                                    value = stockInfo.fiftyTwoWeekLow.toString()
                                )
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
                                        Column(
                                            modifier = Modifier,
                                            horizontalAlignment = Alignment.CenterHorizontally
                                        ) {
                                            Text(
                                                stockInfo.price.toString(), style = TextStyle(
                                                    platformStyle = PlatformTextStyle(
                                                        includeFontPadding = false
                                                    ), fontSize = 10.sp
                                                )
                                            )
                                            Icon(
                                                Icons.Rounded.ArrowDropDown,
                                                contentDescription = "Logo",
                                                Modifier
                                                    .scale(1f)
                                                    .clip(CircleShape)
                                            )
                                        }
                                    }
                                )
                                MetricVisual(
                                    alignment = Alignment.End,
                                    metricName = "52-Week High",
                                    value = stockInfo.fiftyTwoWeekHigh.toString()
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(Alignment.CenterHorizontally),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val list = listOf(
                                    Pair(
                                        "Market Cap",
                                        formatNumber(
                                            (stockInfo.marketCapitalization).toString()
                                        )
                                    ),
                                    Pair("P/E Ratio", stockInfo.peRatio),
                                    Pair("Beta", stockInfo.beta),
                                    Pair("Dividend Yield", "${stockInfo.dividendYield}%"),
                                    Pair("Profit Margin", stockInfo.profitMargin)
                                )
                                list.forEach {
                                    MetricVisual(
                                        Modifier,
                                        alignment = Alignment.Start,
                                        metricName = it.first,
                                        value = it.second.toString()
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
}

@Composable
fun formatNumber(value:String):String{
    if(value.length>12)
        return value.substring(0,value.length-12)+"T"
    else if(value.length>9)
        return value.substring(0,value.length-9)+"B"
    else if(value.length>6)
        return value.substring(0,value.length-6)+"M"
    else
        return value
}

@Composable
private fun MetricVisual(modifier: Modifier=Modifier,alignment: Alignment.Horizontal, metricName:String, value:String){
    Column(modifier = modifier.padding(vertical = 8.dp, horizontal = 4.dp), horizontalAlignment = alignment, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = metricName,
            textAlign = if(alignment==Alignment.Start) TextAlign.Start else TextAlign.End,
            fontSize = 11.sp,
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

private fun getValuePercentageForRange(value: Double, max: Double, min: Double) =
    (value - min) / (max - min)

fun getSecondMinValue(list: List<Float>): Float {
    val minObject = list.minByOrNull { it }
    return list.minByOrNull { it -> if (it == minObject) Float.MAX_VALUE else it }?:1f
}

@Composable
fun PerformanceChart(modifier: Modifier = Modifier, list: List<StockTimeSeriesItem> = emptyList(), isLoading:Boolean
//    listOf(10f, 20f, 3f, 1f,10f,20f,30f,10f,22f,22.4f,22f,10f,20f,30f,10f,22f,22.4f,22f,10f,20f,30f,10f,22f,22.4f,22f,10f,20f,30f,10f,22f,22.4f,22f,10f,20f,30f,10f,22f,22.4f,22f)
) {
//    val zipList: List<Pair<Float, Float>> = list.zipWithNext()

    val datapoint= mutableListOf<Pair<Double, StockTimeSeriesItem>>()
    list.forEach {
        val sdf: SimpleDateFormat = SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
        val mDate: Date? = sdf.parse(it.timestamp)
        val timeInMilliseconds: Long = mDate?.time ?:0
        datapoint.add(Pair(timeInMilliseconds.toDouble(),it))
    }

    val real= mutableListOf<Pair<Double,Double>>()
    datapoint.sortBy { it.first }
    datapoint.forEachIndexed{idx,item ->
        if((idx+1)%(60)==0){
            real.add(Pair(item.first,item.second.close))
        }
    }

    if (isLoading||list.isEmpty()) {
        Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            CircularProgressIndicator(
                trackColor = Color.Green,
                modifier = Modifier.size(50.dp)
            )
        }
    } else {
    Row(modifier = modifier.padding(6.dp)) {

//        Log.e("LineGraph",list.toString())

        val modelProducer = remember { CartesianChartModelProducer.build() }
//        LaunchedEffect(Unit) {
            modelProducer.tryRunTransaction {
            lineSeries {
                series(
                    y = real.map { it.second },
                    x = real.map { it.first },
                )
            }
            }
//        }
        CartesianChartHost(
            rememberCartesianChart(
                rememberLineCartesianLayer(spacing = 20.dp, lines = listOf(
                    rememberLineSpec(
                        shader = DynamicShader.color(Color.Green),
                        pointConnector = DefaultPointConnector(cubicStrength = 0f),
                ))),
                startAxis = rememberStartAxis(guideline = null),
                bottomAxis = rememberBottomAxis(guideline = null, itemPlacer = remember {
                    AxisItemPlacer.Horizontal.default(spacing = list.size)
                },
                    titleComponent =
                    rememberTextComponent(
                        background = rememberShapeComponent(Shape.Pill, Color.Green),
                        color = Color.White,
                        padding = Dimensions.of(horizontal = 8.dp, vertical = 2.dp),
                        margins = Dimensions.of(top = 4.dp),
                        typeface = android.graphics.Typeface.MONOSPACE,
                    ),
                    title = "Time Stamp"),
                fadingEdges = rememberFadingEdges()
            ),modelProducer, modifier = Modifier.fillMaxSize(),
            marker = rememberMarker(),
            zoomState = rememberVicoZoomState(true),
        )
    }}

}


@Composable
fun PerformanceChart2(modifier: Modifier = Modifier, list: List<StockTimeSeriesItem> = emptyList(), isLoading:Boolean) {

    val datapoint= mutableListOf<Pair<Double, StockTimeSeriesItem>>()
    list.forEach {
        val sdf: SimpleDateFormat = SimpleDateFormat("YYYY-MM-dd HH:mm:ss")
        val mDate: Date? = sdf.parse(it.timestamp)
        val timeInMilliseconds: Long = mDate?.time ?:0
        datapoint.add(Pair(timeInMilliseconds.toDouble(),it))
    }

    val real= mutableListOf<Pair<Double,Double>>()
    datapoint.sortBy { it.first }
    datapoint.forEachIndexed{idx,item ->
        if((idx+1)%(60)==0){
            real.add(Pair(item.first,item.second.close))
        }
    }

    if (isLoading||list.isEmpty()) {
        Column(modifier = modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            CircularProgressIndicator(
                trackColor = Color.Green,
                modifier = Modifier.size(50.dp)
            )
        }
    } else {
        Row(modifier = modifier.padding(6.dp)) {

        var isVisual by remember {
            mutableFloatStateOf(0f)
        }

        Text(text = "$isVisual", fontSize = 20.sp, color = Color.Magenta)

        val max = real.map{it.second}.max()
        val min =  real.map{it.second}.min()

        val lineColor =
            Color.Black// <-- Line color is Green if its going up and Red otherwise

        for (pair in real) {

            val fromValuePercentage = getValuePercentageForRange(pair.first, max, min)
            val toValuePercentage = getValuePercentageForRange(pair.second, max, min)

            Canvas(
                modifier = Modifier
                    .background(Color.White)
                    .pointerInput(key1 = "someStringKey?") {
                        detectTapGestures(
                            onPress = { isVisual = pair.first.toFloat() }
                        )
                    }
                    .fillMaxHeight()
                    .weight(1f),
                onDraw = {
                    val fromPoint = Offset(
                        x = 0f,
                        y = size.height.times(1 - fromValuePercentage).toFloat()
                    ) // <-- Use times so it works for any available space
                    val toPoint =
                        Offset(
                            x = size.width,
                            y = size.height.times(1 - toValuePercentage).toFloat()
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
}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    StocksAppTheme {
//        DetailScreen(
//            Modifier
//                .padding(0.dp)
//                .fillMaxSize(),
//            stockViewModel
//        )
    }
}