package com.example.foodcare.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodcare.data.api.SessaoUsuario
import com.example.foodcare.data.repository.UsuarioRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException

class LoginViewModel() : ViewModel()
{
    var email by mutableStateOf("")
    var senha by mutableStateOf("")
    var senhaVisivel by mutableStateOf(false)
    var loginSucesso by mutableStateOf(false)
        private set

    var mensagemErro by mutableStateOf("")
        private set



    private val repository = UsuarioRepository()

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
                    SessaoUsuario.tipoUsuario = resposta.tipoUsuario
                    loginSucesso = true

                }catch (e: ConnectException) {
                    mensagemErro = "Servidor indisponível"
                }
                catch (e: HttpException) {

                    if (e.code() == 401) {

                        mensagemErro = "Email ou senha incorretos."

                    }

                } catch (e: Exception) {
                    mensagemErro = e.message.toString()
                }
            }
        }
    }
}