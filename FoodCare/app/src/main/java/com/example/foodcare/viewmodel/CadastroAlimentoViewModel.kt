package com.example.foodcare.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodcare.data.api.SessaoUsuario
import com.example.foodcare.data.api.RetrofitClient
import com.example.foodcare.data.repository.AlimentoRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import com.example.foodcare.model.*

class CadastroAlimentoViewModel : ViewModel() {

    var nome by mutableStateOf("")
    var descricao by mutableStateOf("")
    var quantidade by mutableStateOf("")
    var validade by mutableStateOf("")


    var carregando by mutableStateOf(false)
        private set

    var mensagemErro by mutableStateOf("")
        private set

    var cadastroSucesso by mutableStateOf(false)
        private set

    var categoriaSelecionada by mutableStateOf<CategoriaResposta?>(null)
    var listaCategorias by mutableStateOf<List<CategoriaResposta>>(emptyList())
        private set
    var carregandoCategorias by mutableStateOf(false)
        private set

    init {
        buscarCategorias()
    }

    private val repository = AlimentoRepository()

    private fun buscarCategorias() {
        viewModelScope.launch {
            try {
                carregandoCategorias = true
                listaCategorias = RetrofitClient.api.listarCategorias();
            } catch (e: Exception) {
                mensagemErro = "Não foi possível carregar as categorias."
            } finally {
                carregandoCategorias = false
            }
        }
    }



    fun cadastrar(idCategoria: Int) {
        if (nome.isBlank() || quantidade.isBlank() || validade.isBlank() || idCategoria == 0) {
            mensagemErro = "Preencha todos os campos obrigatórios."
            return
        }

        viewModelScope.launch {
            try {
                carregando = true
                mensagemErro = ""

                val nomeDoDoador = SessaoUsuario.nomeUsuario ?: ""

                val resposta = repository.cadastrar(
                    nomeDoador = nomeDoDoador,
                    nome = nome,
                    descricao = descricao,
                    quantidade = quantidade.toIntOrNull() ?: 0,
                    validade = validade,
                    idCategoria = idCategoria
                )

                cadastroSucesso = true

            } catch (e: HttpException) {
                mensagemErro = when (e.code()) {
                    409 -> "Você já cadastrou um alimento com este nome."
                    404 -> "Doador não encontrado no sistema."
                    else -> "Erro no servidor: ${e.code()}"
                }
            } catch (e: Exception) {
                mensagemErro = "Erro de conexão."
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