package com.example.yummybites

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.yummybites.navigation.YummyBitesNavigation
import com.example.yummybites.ui.theme.YummyBitesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YummyBitesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    YummyBitesApp()
                }
            }
        }
    }
}

@Composable
fun YummyBitesApp() {
   Surface(modifier = Modifier
       .fillMaxWidth()
       .background(MaterialTheme.colors.background)
       .padding(0.dp)) {
       YummyBitesNavigation()

   }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    YummyBitesTheme {
       YummyBitesApp()
    }
}