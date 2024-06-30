package com.example.stocksapp.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.stocksapp.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(modifier: Modifier,route:String){
    Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically,
       modifier = modifier
           .fillMaxWidth()
           .padding(10.dp)) {
        if(route == Screen.HomeScreen.route){
            Text(modifier = Modifier.fillMaxWidth(),text = "STOCKS APP", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
        else{
            Text(
                text = "Details Screen",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth(0.4f)
            )
           DockedSearchBar(modifier = Modifier
               .height(30.dp)
               .fillMaxWidth(0.93f),
               query = "",
               onQueryChange = {},
               onSearch = {},
               active =false ,
               onActiveChange = {}
           ) {

           }
        }
    }
}

@Preview("she")
@Composable
fun aheeh(){
 TopBar(modifier = Modifier.fillMaxSize(), route ="detail_screen" )
}