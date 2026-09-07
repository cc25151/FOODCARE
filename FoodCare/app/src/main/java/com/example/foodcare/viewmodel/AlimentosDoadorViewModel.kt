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
    var erroEdicao by mutableStateOf("")

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
                erroEdicao = "" // Limpa o erro anterior antes de tentar
                val resposta = repository.atualizarAlimento(alimento.idAlimento, novo)

                if (resposta) {
                    alimentoSelecionado = null // Fecha o dialog se deu certo
                    carregarAlimentos()       // Recarrega a lista
                } else {

                    erroEdicao = "Não foi possível atualizar o alimento."
                }
            } catch (e: Exception) {
                erroEdicao = "Erro ao conectar ao servidor. Tente novamente."
            }
        }
    }
}
