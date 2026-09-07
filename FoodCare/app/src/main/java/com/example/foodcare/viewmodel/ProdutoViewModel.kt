package com.example.foodcare.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodcare.data.api.SessaoUsuario
import com.example.foodcare.data.repository.AlimentoRepository
import com.example.foodcare.data.repository.DoadorRepository
import com.example.foodcare.model.Doador
import com.example.foodcare.model.Produto
import kotlinx.coroutines.launch

class ProdutoViewModel : ViewModel() {
    val repository = AlimentoRepository()
    val repositoryDoador = DoadorRepository()
    var produto by mutableStateOf<Produto?>(null)
    var doador by mutableStateOf<Doador?>(null)


    fun carregarProduto(id: Int){
        viewModelScope.launch {
            try {
                val produtoObtido = repository.getProduto(id)
                produto = produtoObtido


                if (produtoObtido != null && produtoObtido.idDoador != 0) {
                    doador = repositoryDoador.getDoador(produtoObtido.idDoador)
                }
            } catch (e: Exception) {
                println("Erro ao carregar: ${e.localizedMessage}")
                e.printStackTrace()
            }
        }
    }
}