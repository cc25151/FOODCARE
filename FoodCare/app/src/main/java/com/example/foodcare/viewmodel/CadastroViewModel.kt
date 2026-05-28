package com.example.foodcare.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodcare.data.api.SessaoUsuario
import com.example.foodcare.data.repository.UsuarioRepository
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


    private val repository = UsuarioRepository()

    var cadastroSucesso by mutableStateOf(false)
        private set

    fun FazerCadastro() {
        if (nome.isBlank() || email.isBlank() || senha.isBlank() || tipoPessoa.isBlank() || documento.isBlank()) {
            mensagemErro = "Preencha todos os campos."
            return
        }


        viewModelScope.launch {
            try {
                val resposta =
                    repository.cadastro(nome, email, senha, tipoPessoa, documento)
                when (SessaoUsuario.tipoUsuario) {
                    "doador" -> {
                        repository.cadastrarDoador(resposta.idUsuario)
                    }

                    "receptor" -> {
                        repository.cadastrarReceptor(resposta.idUsuario)
                    }

                    "ambos" -> {
                        repository.cadastrarDoador(resposta.idUsuario)
                        repository.cadastrarReceptor(resposta.idUsuario)
                    }
                }

                //faz o login logo após o cadastro
                val respostaLogin = repository.login(email, senha)
                SessaoUsuario.idUsuario = respostaLogin.idUsuario
                SessaoUsuario.token = respostaLogin.token
                SessaoUsuario.nomeUsuario = respostaLogin.nome

                cadastroSucesso = true
            }
            catch (e: ConnectException) {
                mensagemErro = "Servidor indisponível"
            }
            catch (e: HttpException) {
                if (e.code() == 400)
                    mensagemErro = "Credenciais já estão em uso."
                else
                    mensagemErro = "${e.code()}"
            }
            catch(e: Exception){
                mensagemErro = "Erro: ${e.message}"
            }

        }
    }
}