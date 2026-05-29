package com.example.foodcare.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.foodcare.data.api.SessaoUsuario
import com.example.foodcare.data.repository.UsuarioRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class PerfilViewModel(): ViewModel(){

    var nome by mutableStateOf("")
    var email by mutableStateOf("")
    var documento by mutableStateOf("")
    var rua by mutableStateOf("")
    var numero by mutableStateOf("")
    var cidade by mutableStateOf("")
    var bairro by mutableStateOf("")
    var cep by mutableStateOf("")
    var tipoPessoa by mutableStateOf("")
    var pontuacao: Double? by mutableStateOf(0.0)

    val repository = UsuarioRepository()

    fun carregarPerfil(){
        try{
            viewModelScope.launch{
                val resposta = repository.getDados(SessaoUsuario.idUsuario)
                resposta.run{
                    this@PerfilViewModel.nome = nome
                    this@PerfilViewModel.email = email
                    this@PerfilViewModel.documento = documento
                    this@PerfilViewModel.rua = rua ?: ""
                    this@PerfilViewModel.numero = numero ?: ""
                    this@PerfilViewModel.cidade = cidade ?: ""
                    this@PerfilViewModel.bairro = bairro ?: ""
                    this@PerfilViewModel.cep = cep ?: ""
                    this@PerfilViewModel.tipoPessoa = tipoPessoa

                }
            }

        }
        catch(e: Exception){

        }
    }

    fun logout(){
        // zera todos os atributos da sessão atual
        SessaoUsuario.idUsuario = 0
        SessaoUsuario.nomeUsuario = ""
        SessaoUsuario.token = ""
        SessaoUsuario.tipoUsuario = ""
    }


}