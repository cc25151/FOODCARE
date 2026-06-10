package com.example.foodcare.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodcare.data.api.RetrofitInstance
import com.example.foodcare.data.api.SessaoUsuario
import com.example.foodcare.model.AlimentoDoadorUi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AlimentoDoadorUi(
    val idAlimento: Int,
    val nome: String,
    val categoria: String,
    val qntd: Int,
    val descricao: String,
    val validade: String
)

data class EdicaoAlimentoForm(
    val qntd: String = "",
    val validade: String = "",
    val descricao: String = ""
)

sealed class AlimentosUiState {
    object Loading : AlimentosUiState()
    data class Success(val alimentos: List<AlimentoDoadorUi>) : AlimentosUiState()
    data class Error(val mensagem: String) : AlimentosUiState()
}

sealed class EdicaoUiState {
    object Idle : EdicaoUiState()
    object Salvando : EdicaoUiState()
    object Sucesso : EdicaoUiState()
    data class Erro(val mensagem: String) : EdicaoUiState()
}

class AlimentosDoadorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<AlimentosUiState>(AlimentosUiState.Loading)
    val uiState: StateFlow<AlimentosUiState> = _uiState.asStateFlow()

    private val _alimentoSelecionado = MutableStateFlow<AlimentoDoadorUi?>(null)
    val alimentoSelecionado: StateFlow<AlimentoDoadorUi?> = _alimentoSelecionado.asStateFlow()

    private val _edicaoForm = MutableStateFlow(EdicaoAlimentoForm())
    val edicaoForm: StateFlow<EdicaoAlimentoForm> = _edicaoForm.asStateFlow()

    private val _edicaoUiState = MutableStateFlow<EdicaoUiState>(EdicaoUiState.Idle)
    val edicaoUiState: StateFlow<EdicaoUiState> = _edicaoUiState.asStateFlow()

    init {
        carregarAlimentos()
    }

    fun carregarAlimentos() {
        viewModelScope.launch {
            _uiState.value = AlimentosUiState.Loading
            try {
                val idUsuario = SessaoUsuario.idUsuario
                val response = RetrofitInstance.api.getAlimentosDoador(idUsuario)
                if (response.isSuccessful) {
                    val lista = response.body()?.map { dto ->
                        AlimentoDoadorUi(
                            idAlimento = dto.idAlimento,
                            nome       = dto.nome,
                            categoria  = dto.categoria,
                            qntd       = dto.qntd,
                            descricao  = dto.descricao,
                            validade   = dto.validade
                        )
                    } ?: emptyList()
                    _uiState.value = AlimentosUiState.Success(lista)
                } else {
                    _uiState.value = AlimentosUiState.Error("Erro ao carregar alimentos.")
                }
            } catch (e: Exception) {
                _uiState.value = AlimentosUiState.Error("Sem conexão com o servidor.")
            }
        }
    }

    fun abrirEdicao(alimento: AlimentoDoadorUi) {
        _alimentoSelecionado.value = alimento
        _edicaoForm.value = EdicaoAlimentoForm(
            qntd      = alimento.qntd.toString(),
            validade  = alimento.validade,
            descricao = alimento.descricao
        )
        _edicaoUiState.value = EdicaoUiState.Idle
    }

    fun fecharEdicao() {
        _alimentoSelecionado.value = null
        _edicaoForm.value = EdicaoAlimentoForm()
        _edicaoUiState.value = EdicaoUiState.Idle
    }

    fun onQntdChange(valor: String) {
        _edicaoForm.value = _edicaoForm.value.copy(qntd = valor)
    }

    fun onValidadeChange(valor: String) {
        _edicaoForm.value = _edicaoForm.value.copy(validade = valor)
    }

    fun onDescricaoChange(valor: String) {
        _edicaoForm.value = _edicaoForm.value.copy(descricao = valor)
    }

    fun salvarEdicao() {
        val alimento = _alimentoSelecionado.value ?: return
        val form = _edicaoForm.value

        val qntdInt = form.qntd.toIntOrNull()
        if (qntdInt == null || qntdInt <= 0) {
            _edicaoUiState.value = EdicaoUiState.Erro("Quantidade inválida.")
            return
        }
        if (form.validade.isBlank()) {
            _edicaoUiState.value = EdicaoUiState.Erro("Informe a validade.")
            return
        }
        if (form.descricao.isBlank()) {
            _edicaoUiState.value = EdicaoUiState.Erro("Informe a descrição.")
            return
        }

        viewModelScope.launch {
            _edicaoUiState.value = EdicaoUiState.Salvando
            try {
                val body = mapOf(
                    "qntd"      to qntdInt,
                    "validade"  to form.validade,
                    "descricao" to form.descricao
                )
                val response = RetrofitInstance.api.editarAlimento(alimento.idAlimento, body)
                if (response.isSuccessful) {
                    _edicaoUiState.value = EdicaoUiState.Sucesso
                    fecharEdicao()
                    carregarAlimentos()
                } else {
                    _edicaoUiState.value = EdicaoUiState.Erro("Não foi possível salvar.")
                }
            } catch (e: Exception) {
                _edicaoUiState.value = EdicaoUiState.Erro("Sem conexão com o servidor.")
            }
        }
    }
}
