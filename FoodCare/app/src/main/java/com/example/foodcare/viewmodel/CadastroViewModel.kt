package com.example.foodcare.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodcare.data.repository.CadastroRepository
import com.example.foodcare.model.CadastroRequest
import kotlinx.coroutines.launch

class CadastroViewModel : ViewModel() {
    var nome by mutableStateOf("")
    var  email by mutableStateOf("")
    var senha by mutableStateOf("")
    var documento by mutableStateOf("")

    var tipoPessoa by mutableStateOf("")
    var mensagemErro by mutableStateOf("")

    private val cadastroRepository = CadastroRepository()

    fun FazerCadastro() {
        if (nome.isBlank() || email.isBlank() || senha.isBlank() || tipoPessoa.isBlank() || documento.isBlank()) {
            mensagemErro = "Preencha todos os campos!"

        }

        mensagemErro

        viewModelScope.launch {
            try {
                val resposta = cadastroRepository.cadastro(nome, email, senha, tipoPessoa, documento)
            } catch (e: Exception) {
                mensagemErro = "Erro ao realizar cadastro: ${e}"
            }
        }
    }
}