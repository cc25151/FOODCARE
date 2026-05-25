package com.example.foodcare.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.foodcare.data.api.SessaoUsuario

class FinalidadeViewModel : ViewModel() {
    var tipoUsuario by mutableStateOf("");
    }
