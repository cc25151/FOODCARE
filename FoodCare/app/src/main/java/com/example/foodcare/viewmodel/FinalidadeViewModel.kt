package com.example.foodcare.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.foodcare.data.api.SessaoUsuario

class FinalidadeViewModel : ViewModel() {

    var tipoUsuario by mutableStateOf("")

    var mensagemErro by mutableStateOf("")
        private set

    var finalidadeSucesso by mutableStateOf(false)
        private set

    fun salvarFinalidade() {
        if (tipoUsuario.isBlank()) {
            mensagemErro = "Por favor, selecione uma das opções."
            return
        }

        mensagemErro = ""


        finalidadeSucesso = true
    }
}