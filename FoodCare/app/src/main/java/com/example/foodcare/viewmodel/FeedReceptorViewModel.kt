package com.example.foodcare.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodcare.data.api.SessaoUsuario
import com.example.foodcare.data.repository.AlimentoRepository
import com.example.foodcare.data.repository.CategoriaRepository
import com.example.foodcare.model.CategoriaResposta
import com.example.foodcare.model.Produto
import kotlinx.coroutines.launch


class FeedReceptorViewModel (): ViewModel(){
    var busca by mutableStateOf("")

    var categoriaSelecionada by mutableStateOf<Int?>(null)

    var alimentos by mutableStateOf<List<Produto>>(emptyList())


    var mensagemErro by mutableStateOf<String?>(null)

    var categorias by mutableStateOf<List<CategoriaResposta>>(emptyList())

    val repository = AlimentoRepository()
    val repositoryCat = CategoriaRepository()

    val produtosFiltrados: List<Produto>
        get() = alimentos.filter { produto ->

            val passaBusca =
                busca.isBlank() ||
                        produto.nome.contains(busca, true)

            val passaCategoria =
                when (categoriaSelecionada) {

                    null -> true // Todos

                    0 -> produto.distancia <= 3 // Recomendados

                    else -> produto.idCategoria == categoriaSelecionada
                }


            passaBusca && passaCategoria
        }

    fun CarregarPagina(){
        viewModelScope.launch {
            try{
                alimentos = repository.getAlimentos(SessaoUsuario.idUsuario)
                categorias = repositoryCat.BuscarCategorias()
            }catch (e: Exception){
                mensagemErro = "Não foi possível carregar os alimentos."
            }

        }
    }
}