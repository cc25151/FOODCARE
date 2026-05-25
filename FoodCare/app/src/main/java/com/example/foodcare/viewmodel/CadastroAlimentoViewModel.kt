package com.example.foodcare.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodcare.data.api.SessaoUsuario
import com.example.foodcare.data.repository.LoginRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException

class CadastroAlimentoViewModel : ViewModel() {
    // UI
    var nome by mutableStateOf("")
    var descricao by mutableStateOf("")
    var quantidade by mutableStateOf("")
    var validade by mutableStateOf("")

    // Controle
    var carregando by mutableStateOf(false)
        private set

    var mensagemErro by mutableStateOf("")
        private set

    var cadastroSucesso by mutableStateOf(false)
        private set

    var categoriaSelecionada by mutableStateOf<CategoriaUi?>(null)

    // private val repository = AlimentoRepository()

    fun cadastrar(idCategoria: Int) {
        if (nome.isBlank() || quantidade.isBlank() || validade.isBlank() || idCategoria == 0) {
            mensagemErro = "Preencha todos os campos obrigatórios."
            return
        }

        viewModelScope.launch {
            try {
                carregando = true
                mensagemErro = ""

                // Converte tipo
                val qtdInt = quantidade.toIntOrNull() ?: 0

                /*
                val resposta = repository.cadastrarAlimento(
                    token = SessaoUsuario.token ?: "",
                    nome = nome,
                    descricao = descricao,
                    quantidade = qtdInt,
                    validade = validade,
                    idCategoria = idCategoria
                )
                */

                cadastroSucesso = true

            } catch (e: HttpException) {
                mensagemErro = when (e.code()) {
                    400 -> "Dados inválidos. Verifique as informações."
                    401 -> "Sessão expirada. Faça login novamente."
                    else -> "Erro no servidor: ${e.code()}"
                }
            } catch (e: Exception) {
                mensagemErro = "Erro de conexão: Verifique sua internet."
            } finally {
                carregando = false
            }
        }
    }

    fun resetarFormulario() {
        nome = ""
        descricao = ""
        quantidade = ""
        validade = ""
        categoriaSelecionada = null
        cadastroSucesso = false
    }
}