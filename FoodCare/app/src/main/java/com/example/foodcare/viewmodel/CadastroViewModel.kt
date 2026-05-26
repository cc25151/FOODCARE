package com.example.foodcare.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodcare.data.api.SessaoUsuario
import com.example.foodcare.data.repository.CadastroRepository
import com.example.foodcare.data.repository.LoginRepository
import com.example.foodcare.model.CadastroRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException

class CadastroViewModel : ViewModel() {
    var nome by mutableStateOf("")
    var  email by mutableStateOf("")
    var senha by mutableStateOf("")

    var senhaVisivel by  mutableStateOf(false)
    var documento by mutableStateOf("")

    var tipoPessoa by mutableStateOf("")
    var mensagemErro by mutableStateOf("")

    var tipoUsuario by mutableStateOf("")

    private val cadastroRepository = CadastroRepository()

    private val loginRepository = LoginRepository()

    var cadastroSucesso by mutableStateOf(false)
        private set

    fun FazerCadastro() {
        if (nome.isBlank() || email.isBlank() || senha.isBlank() || tipoPessoa.isBlank() || documento.isBlank()) {
            mensagemErro = "Preencha todos os campos."

        }

        else {
            viewModelScope.launch {
                try {
                    val resposta =
                        cadastroRepository.cadastro(nome, email, senha, tipoPessoa, documento)
                    when (tipoUsuario) {
                        "doador" -> {
                            cadastroRepository.cadastrarDoador(resposta.idUsuario)
                        }

                        "receptor" -> {
                            cadastroRepository.cadastrarReceptor(resposta.idUsuario)
                        }

                        "ambos" -> {
                            cadastroRepository.cadastrarDoador(resposta.idUsuario)
                            cadastroRepository.cadastrarReceptor(resposta.idUsuario)
                        }
                    }

                    //faz o login logo após o cadastro
                    val respostaLogin = loginRepository.login(email, senha)
                    SessaoUsuario.idUsuario = respostaLogin.idUsuario
                    SessaoUsuario.token = respostaLogin.token
                    SessaoUsuario.nomeUsuario = respostaLogin.nome

                    cadastroSucesso = true
                }
                catch (e: ConnectException) {
                    mensagemErro = "Servidor indisponível"
                }
                catch (e: HttpException) {
                    val code = e.code()
                    val errorBody = e.response()?.errorBody()?.string()

                    mensagemErro = "HTTP $code - $errorBody"
                }
                catch(e: Exception){
                    mensagemErro = "Erro: ${e.message}"
                }

            }
        }
    }
}