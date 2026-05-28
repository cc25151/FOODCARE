package com.example.foodcare.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodcare.data.api.SessaoUsuario
import com.example.foodcare.data.repository.LoginRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel() : ViewModel()
{
    var email by mutableStateOf("")
    var senha by mutableStateOf("")
    var senhaVisivel by mutableStateOf(false)
    var loginSucesso by mutableStateOf(false)
        private set

    var mensagemErro by mutableStateOf("")
        private set

    var qualTipoUsuario by mutableStateOf("")


    private val repository = LoginRepository()

    fun FazerLogin(){
        viewModelScope.launch{
            if (email == "" || senha == "") mensagemErro = "Preencha todos os campos."

            else{
                try {

                    val resposta =
                        repository.login(
                            email,
                            senha
                        )


                    SessaoUsuario.idUsuario = resposta.idUsuario
                    SessaoUsuario.token = resposta.token
                    SessaoUsuario.nomeUsuario = resposta.nome

                    val responseDoador = repository.verificarDoador(resposta.idUsuario)

                    if (responseDoador.isSuccessful && responseDoador.body() != null) {
                        qualTipoUsuario = "doador"
                        loginSucesso = true

                        val responseReceptor = repository.verificarReceptor(resposta.idUsuario)

                        if (responseReceptor.isSuccessful && responseReceptor.body() != null) {
                            qualTipoUsuario = "receptor"
                            loginSucesso = true
                        } else {
                            mensagemErro = "Perfil de usuário não encontrado."
                        }
                    }
                } catch (e: HttpException) {

                    if (e.code() == 401) {

                        mensagemErro = "Email ou senha incorretos."

                    }

                } catch (e: Exception) {

                    mensagemErro = "Erro de conexão"

                }
            }
        }
    }
}