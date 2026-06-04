package com.example.foodcare.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.foodcare.data.api.SessaoUsuario
import com.example.foodcare.data.repository.UsuarioRepository
import androidx.lifecycle.viewModelScope
import com.example.foodcare.data.repository.GeocodingRepository
import kotlinx.coroutines.launch
import java.net.ConnectException


class PerfilViewModel(): ViewModel(){

    var nome by mutableStateOf("")
    var email by mutableStateOf("")
    var documento by mutableStateOf("")
    var rua by mutableStateOf("")
    var numero by mutableStateOf("")
    var cidade by mutableStateOf("")
    var bairro by mutableStateOf("")
    var cep by mutableStateOf("")
    var tipoPessoa by mutableStateOf("")
    var pontuacao: Double? by mutableStateOf(0.0)

    var modoEdicao by mutableStateOf(false)

    var mensagemSucesso by mutableStateOf(false)

    val repository = UsuarioRepository()
    val coordRepository = GeocodingRepository()

    var mensagemErro by mutableStateOf<String?>(null)

    fun carregarPerfil(){

        viewModelScope.launch{
            try{
                val resposta = repository.getDados(SessaoUsuario.idUsuario)
                resposta.run{
                    this@PerfilViewModel.nome = nome
                    this@PerfilViewModel.email = email
                    this@PerfilViewModel.documento = documento
                    this@PerfilViewModel.rua = rua ?: ""
                    this@PerfilViewModel.numero = numero ?: ""
                    this@PerfilViewModel.cidade = cidade ?: ""
                    this@PerfilViewModel.bairro = bairro ?: ""
                    this@PerfilViewModel.cep = "${cep?.substring(0, 5)}-${cep?.substring(5)}"
                    this@PerfilViewModel.tipoPessoa = tipoPessoa
                }
            }
            catch (e : Exception){
                mensagemErro = "Não foi possível salvar as alterações."
            }
        }
    }

    fun salvarPerfil(){
        // quando o usuário clica no botão editar pela primeira vez, modoEdicao é true, e o programa está esperando as edições por parte do usuario.
        // quando o usuario clica pela segunda vez, modoEdicao vira false, e o programa deve salvar as informações
        viewModelScope.launch{
            try{
                val enderecoCompleto = "$rua, $cidade, $cep"
                val coordenadas = coordRepository.getCoordenadas(enderecoCompleto)

                Log.d("GEO", "Endereço: $enderecoCompleto")
                Log.d("GEO", "Coordenadas: $coordenadas")

                if (coordenadas == null) {
                    mensagemErro = "Não foi possível localizar o endereço."
                    return@launch
                }

                val resposta = repository.completarPerfil(
                    SessaoUsuario.idUsuario,
                    nome,
                    email,
                    cep.replace("-", ""),
                    cidade,
                    bairro,
                    rua,
                    numero,
                    coordenadas.latitude,
                    coordenadas.longitude
                    )
                if(resposta){
                    mensagemSucesso = true
                    carregarPerfil()
                }else{
                    mensagemErro = "Não foi possível salvar as alterações."
                    modoEdicao = !modoEdicao // volta para o estado anterior, já que modoEdicao muda ao clicar no botao
                }
            }catch (e: ConnectException) {

                mensagemErro = "Servidor indisponível"
            }
            catch(e : Exception){
                mensagemErro = "Localização inválida."
                modoEdicao = !modoEdicao
            }
        }
    }

    fun logout(){
        // zera todos os atributos da sessão atual
        SessaoUsuario.idUsuario = 0
        SessaoUsuario.nomeUsuario = ""
        SessaoUsuario.token = ""
        SessaoUsuario.tipoUsuario = ""
    }


}