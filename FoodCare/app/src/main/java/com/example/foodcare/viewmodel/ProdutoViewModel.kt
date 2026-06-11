package com.example.foodcare.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodcare.data.repository.AlimentoRepository
import com.example.foodcare.model.Produto
import kotlinx.coroutines.launch

class ProdutoViewModel : ViewModel() {
    val repository = AlimentoRepository()
    var produto = Produto()



    fun carregarProduto(id: Int){
        viewModelScope.launch {
            try{
                produto = repository.getProduto(id)
            }catch(e : Exception){
                println("Erro ao carregar: ${e.localizedMessage}")
            }

        }
    }
}