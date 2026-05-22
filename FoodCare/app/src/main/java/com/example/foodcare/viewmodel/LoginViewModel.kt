package com.example.foodcare.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodcare.data.repository.LoginRepository
import kotlinx.coroutines.launch

class LoginViewModel() : ViewModel()
{
    var email by mutableStateOf("")
    var senha by mutableStateOf("")
    var senhaVisivel by mutableStateOf(false)

    private val repository =
        LoginRepository()

    fun FazerLogin(): Boolean{
        viewModelScope.launch{

            try{

                val resposta =
                    repository.login(
                        email,
                        senha
                    )


                //token recebido:
                println(resposta.token)

            }
            catch(e:Exception){


            }
        }
    }
}