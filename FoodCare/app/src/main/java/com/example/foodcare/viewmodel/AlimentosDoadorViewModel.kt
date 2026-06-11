package com.example.foodcare.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodcare.data.api.SessaoUsuario
import com.example.foodcare.data.repository.AlimentoRepository
import com.example.foodcare.model.Alimento
import kotlinx.coroutines.launch

class AlimentosDoadorViewModel : ViewModel() {

    var alimentos by mutableStateOf<List<Alimento>>(emptyList())
    var alimentoSelecionado by mutableStateOf<Alimento?>(null)


    var erro by mutableStateOf("")
    val repository = AlimentoRepository()


    fun carregarAlimentos() {
        viewModelScope.launch {
            try {
                alimentos = repository.getAlimentosDoador(SessaoUsuario.idUsuario)
                erro = ""
            } catch (e: Exception) {
                erro = "Sem conexão com o servidor"
            }
        }
    }

    fun salvarEdicao(novo: Alimento) {
        val alimento = alimentoSelecionado ?: return
        viewModelScope.launch {
            try {

                val resposta = repository.atualizarAlimento(
                    alimento.id,
                    novo)
                if (resposta) {
                    alimentoSelecionado = null
                    carregarAlimentos()
                }
            } catch (_: Exception) { }
        }
    }
}
