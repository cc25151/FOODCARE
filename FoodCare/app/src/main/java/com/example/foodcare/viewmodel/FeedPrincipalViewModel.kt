package com.example.foodcare.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.foodcare.view.NavTab

class FeedPrincipalViewModel (){
    var tabAtiva by  mutableStateOf(NavTab.RECEPTOR)
}