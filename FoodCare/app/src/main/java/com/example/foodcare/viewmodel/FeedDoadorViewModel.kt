package com.example.foodcare.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.foodcare.model.DoacaoResposta

class FeedDoadorViewModel : ViewModel() {
    val doacoes by mutableStateOf<List<DoacaoResposta>>(emptyList())

}