package com.jhomilmotors.jhomilmotorsfff

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable

import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.jhomilmotors.jhomilmotorsfff.ui.screens.DetailsProduct.DetailsProductoScreen

import com.jhomilmotors.jhomilmotorsfff.ui.theme.JhomilMotorsShopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JhomilMotorsShopTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                    ) {
                        DetailsProductoScreen()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Poppins $name!",
        modifier = modifier
    )
}
@Composable
@Preview()
fun prev(){
    JhomilMotorsShopTheme{
        Greeting("Grupo JhomilMotors")
    }
}