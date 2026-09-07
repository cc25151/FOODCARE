package com.example.foodcare.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodcare.model.Alimento
import com.example.foodcare.ui.theme.*
import com.example.foodcare.viewmodel.AlimentosDoadorViewModel

@Composable
fun TelaAlimentosDoador(
    onVoltar: () -> Unit = {},
    viewModel: AlimentosDoadorViewModel = viewModel()
) {
    LaunchedEffect(Unit) { viewModel.carregarAlimentos() }

    Scaffold(
        containerColor = DHBrancoAlt,
        topBar = {
            Surface(color = DHBranco, shadowElevation = 3.dp) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.Default.ArrowBackIosNew, "Voltar", tint = DHTexto, modifier = Modifier.size(20.dp))
                    }
                    Spacer(Modifier.width(4.dp))
                    Column {
                        Text("Meus Alimentos", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = DHTexto)
                        Text("Toque em um item para editar", fontSize = 11.sp, color = DHSub)
                    }
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { viewModel.carregarAlimentos() }) {
                        Icon(Icons.Default.Refresh, "Atualizar", tint = DHVermelho, modifier = Modifier.size(22.dp))
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            if (viewModel.erro.isNotBlank()) {
                Text(text = viewModel.erro, modifier = Modifier.align(Alignment.Center), color = DHTexto)
            } else if (viewModel.alimentos.isEmpty()) {
                Text(text = "Nenhum alimento cadastrado", modifier = Modifier.align(Alignment.Center), color = DHTexto)
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Cadastrados", fontSize = 13.sp, color = DHSub, fontWeight = FontWeight.Medium)
                            Surface(shape = RoundedCornerShape(20.dp), color = DHVermelho.copy(alpha = 0.10f)) {
                                Text("${viewModel.alimentos.size} itens", modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = DHVermelho)
                            }
                        }
                    }

                    items(viewModel.alimentos, key = { it.idAlimento }) { alimento ->
                        CardAlimento(alimento = alimento, onItemClick = { viewModel.alimentoSelecionado = it })
                    }
                }
            }


            if (viewModel.alimentoSelecionado != null) {
                DialogEdicaoAlimento(
                    alimento = viewModel.alimentoSelecionado!!,
                    erroMensagem = viewModel.erroEdicao,
                    onSalvar = { alimentoAlterado ->
                        viewModel.salvarEdicao(alimentoAlterado)
                    },
                    onDismiss = {
                        viewModel.alimentoSelecionado = null
                        viewModel.erroEdicao = ""

                    }
                )
            }
        }
    }
}

@Composable
private fun CardAlimento(alimento: Alimento, onItemClick: (Alimento) -> Unit) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DHBranco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onItemClick(alimento) }
    ) {
        Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(DHVermelho.copy(alpha = 0.08f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Fastfood, null, tint = DHVermelho, modifier = Modifier.size(26.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(alimento.nome, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = DHTexto, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(alimento.categoria, fontSize = 11.sp, color = DHSub, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                    Row(modifier = Modifier
                        .background(DHBrancoAlt, RoundedCornerShape(8.dp))
                        .padding(horizontal = 7.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.Inventory2, null, tint = DHSub, modifier = Modifier.size(11.dp))
                        Text("${alimento.qntd} un.", fontSize = 11.sp, color = DHSub, fontWeight = FontWeight.Medium)
                    }

                    Row(modifier = Modifier
                        .background(DHBrancoAlt, RoundedCornerShape(8.dp))
                        .padding(horizontal = 7.dp, vertical = 4.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(Icons.Default.EventBusy, null, tint = DHSub, modifier = Modifier.size(11.dp))
                        Text(alimento.validade, fontSize = 11.sp, color = DHSub, fontWeight = FontWeight.Medium)
                    }
                }
            }
            Box(modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(DHBrancoAlt), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Edit, "Editar", tint = DHVermelho, modifier = Modifier.size(16.dp))
            }
        }
        if (alimento.descricao.isNotBlank()) {
            HorizontalDivider(color = DHCinza, modifier = Modifier.padding(horizontal = 14.dp))
            Text(alimento.descricao, modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp), fontSize = 12.sp, color = DHSub, maxLines = 2, overflow = TextOverflow.Ellipsis, lineHeight = 17.sp)
        }
    }
}

@Composable
fun DialogEdicaoAlimento(
    alimento: Alimento,
    erroMensagem: String,
    onSalvar: (Alimento) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape = RoundedCornerShape(24.dp),
            color = DHBranco,
            shadowElevation = 12.dp
        ) {

            var qntdTexto by remember { mutableStateOf(alimento.qntd.toString()) }
            var validade by remember { mutableStateOf(alimento.validade) }
            var descricao by remember { mutableStateOf(alimento.descricao) }

            Column(modifier = Modifier.padding(24.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Editar alimento", fontWeight = FontWeight.ExtraBold, fontSize = 18.sp, color = DHTexto)
                        Text(alimento.nome, fontSize = 13.sp, color = DHSub, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(DHBrancoAlt)) {
                        Icon(Icons.Default.Close, "Fechar", tint = DHTexto, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Nome", fontSize = 10.sp, color = DHSub, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(4.dp))
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .background(DHBrancoAlt, RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Lock, null, tint = DHSub.copy(alpha = 0.5f), modifier = Modifier.size(13.dp))
                            Text(alimento.nome, fontSize = 13.sp, color = DHSub, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Categoria", fontSize = 10.sp, color = DHSub, fontWeight = FontWeight.SemiBold)
                        Spacer(Modifier.height(4.dp))
                        Row(modifier = Modifier
                            .fillMaxWidth()
                            .background(DHBrancoAlt, RoundedCornerShape(10.dp))
                            .padding(horizontal = 12.dp, vertical = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Lock, null, tint = DHSub.copy(alpha = 0.5f), modifier = Modifier.size(13.dp))
                            Text(alimento.categoria, fontSize = 13.sp, color = DHSub, maxLines = 1, overflow = TextOverflow.Ellipsis)
                        }
                    }
                }

                Spacer(Modifier.height(14.dp))
                HorizontalDivider(color = DHCinza)
                Spacer(Modifier.height(14.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = qntdTexto,
                        onValueChange = { novoTexto ->
                            // Só permite digitar se forem números (evita caracteres inválidos)
                            if (novoTexto.all { it.isDigit() }) {
                                qntdTexto = novoTexto
                            }
                        },
                        label = { Text("Quantidade", fontSize = 11.sp) },
                        leadingIcon = { Icon(Icons.Default.Inventory2, null, tint = DHVermelho, modifier = Modifier.size(17.dp)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    )
                    OutlinedTextField(
                        value = validade,
                        onValueChange = { validade = it },
                        label = { Text("Validade", fontSize = 11.sp) },
                        leadingIcon = { Icon(Icons.Default.EventBusy, null, tint = DHVermelho, modifier = Modifier.size(17.dp)) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = descricao,
                    onValueChange = { descricao = it }, //  Agora atualiza e exibe em tempo real
                    label = { Text("Descrição", fontSize = 11.sp) },
                    leadingIcon = { Icon(Icons.Default.Description, null, tint = DHVermelho, modifier = Modifier.size(17.dp)) },
                    minLines = 3,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(10.dp))

                if (erroMensagem.isNotBlank()) {
                    Text(
                        text = erroMensagem,
                        color = DHVermelho,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.linearGradient(listOf(DHCinza, DHCinza)))
                    ) {
                        Text("Cancelar", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = DHSub)
                    }
                    Button(
                        onClick = {
                            //  Converte de forma segura antes de enviar para o banco
                            val quantidadeFinal = qntdTexto.toIntOrNull() ?: 0

                            val alimentoModificado = alimento.copy(
                                qntd = quantidadeFinal,
                                validade = validade,
                                descricao = descricao
                            )
                            onSalvar(alimentoModificado)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = DHVermelho, contentColor = DHBranco)
                    ) {
                        Icon(Icons.Default.Check, null, modifier = Modifier.size(18.dp))
                        Spacer(Modifier.width(6.dp))
                        Text("Salvar", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}