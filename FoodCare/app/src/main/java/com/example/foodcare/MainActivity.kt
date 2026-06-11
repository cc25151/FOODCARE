package com.example.foodcare

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.foodcare.navigation.FoodCareNavGraph
import com.example.foodcare.ui.theme.FoodCareTheme
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.MapsInitializer.Renderer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Dispatchers.IO) {
            MapsInitializer.initialize(
                applicationContext,
                MapsInitializer.Renderer.LATEST
            ) { renderer ->

            }
        }

        setContent {
            FoodCareTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    FoodCareNavGraph()
                }
            }
        }
    }
}