package com.example.foodcare.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodcare.data.api.SessaoUsuario
import com.example.foodcare.data.repository.DoacaoRepository
import com.example.foodcare.model.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.Locale

class CadastroDoacaoViewModel : ViewModel() {

    var dataDoacao by mutableStateOf("")
    var horarioInicial by mutableStateOf("")
    var horarioFinal by mutableStateOf("")

    var carregando by mutableStateOf(false)
        private set

    var mensagemErro by mutableStateOf("")
        private set

    var cadastroSucesso by mutableStateOf(false)
        private set

    private val repository = DoacaoRepository()

    fun cadastrarDoacao(idAlimentoGerado: Int) {
        if (dataDoacao.isBlank() || horarioInicial.isBlank() || horarioFinal.isBlank()) {
            mensagemErro = "Preencha todos os campos obrigatórios."
            return
        }

        val regexHora = Regex("^([01]\\d|2[0-3]):([0-5]\\d)$")
        if (!regexHora.matches(horarioInicial) || !regexHora.matches(horarioFinal)) {
            mensagemErro = "Formato de hora inválido. Use HH:MM (ex: 08:00)."
            return
        }

        viewModelScope.launch {
            try {
                carregando = true
                mensagemErro = ""

                val idDoadorAtual = SessaoUsuario.idUsuario ?: 0


                val dataFormatada = formatarDataParaIso(dataDoacao)


                val horaInicioFormatada = "$horarioInicial:00"
                val horaFimFormatada = "$horarioFinal:00"


                val request = DoacaoRequest(
                    dataDoacao = dataFormatada,
                    horarioInicial = horaInicioFormatada,
                    horarioFinal = horaFimFormatada,
                    idDoador = idDoadorAtual,
                    idAlimento = idAlimentoGerado,
                    idReceptor = 1,
                    avaliacao = 0
                )

                val resposta = repository.cadastrar(request)

                cadastroSucesso = true

            } catch (e: HttpException) {
                mensagemErro = when (e.code()) {
                    400 -> "Dados inválidos. Verifique as informações."
                    404 -> "Doador ou Alimento não encontrado no sistema."
                    500 -> "Erro interno. Verifique se o Receptor ID 1 existe no banco."
                    else -> "Erro no servidor: ${e.code()}"
                }
            } catch (e: Exception) {
                mensagemErro = "Erro de conexão: ${e.message}"
            } finally {
                carregando = false
            }
        }
    }

    fun resetarFormulario() {
        dataDoacao = ""
        horarioInicial = ""
        horarioFinal = ""
        mensagemErro = ""
        cadastroSucesso = false
    }

    private fun formatarDataParaIso(dataBR: String): String {
        return try {
            val formatoEntrada = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val formatoSaida = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val data = formatoEntrada.parse(dataBR)
            if (data != null) formatoSaida.format(data) else dataBR
        } catch (e: Exception) {
            dataBR
        }
    }
}