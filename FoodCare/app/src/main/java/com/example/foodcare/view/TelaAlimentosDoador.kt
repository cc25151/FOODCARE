package com.example.foodcare.view

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodcare.ui.theme.*
import com.example.foodcare.viewmodel.AlimentoDoadorUi
import com.example.foodcare.viewmodel.AlimentosDoadorViewModel
import com.example.foodcare.viewmodel.AlimentosUiState
import com.example.foodcare.viewmodel.EdicaoUiState

// ─────────────────────────────────────────────
// Paleta de categorias
// ─────────────────────────────────────────────
private val categoriaIcone: Map<String, ImageVector> = mapOf(
    "Marmitas e Refeições Prontas" to Icons.Default.DinnerDining,
    "Padaria e Confeitaria"        to Icons.Default.BakeryDining,
    "Frutas e Hortifruti"          to Icons.Default.Eco,
    "Alimentos Embalados"          to Icons.Default.Inventory2
)

private val categoriaCorFundo: Map<String, Color> = mapOf(
    "Marmitas e Refeições Prontas" to Color(0xFFFFF3E0),
    "Padaria e Confeitaria"        to Color(0xFFFCE4EC),
    "Frutas e Hortifruti"          to Color(0xFFE8F5E9),
    "Alimentos Embalados"          to Color(0xFFE3F2FD)
)

private val categoriaCorIcone: Map<String, Color> = mapOf(
    "Marmitas e Refeições Prontas" to Color(0xFFE65100),
    "Padaria e Confeitaria"        to Color(0xFFC62828),
    "Frutas e Hortifruti"          to Color(0xFF2E7D32),
    "Alimentos Embalados"          to Color(0xFF1565C0)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaAlimentosDoador(
    onVoltar: () -> Unit = {},
    viewModel: AlimentosDoadorViewModel = viewModel()
) {
    val uiState          by viewModel.uiState.collectAsStateWithLifecycle()
    val alimentoSel      by viewModel.alimentoSelecionado.collectAsStateWithLifecycle()
    val edicaoForm       by viewModel.edicaoForm.collectAsStateWithLifecycle()
    val edicaoUiState    by viewModel.edicaoUiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = DHBrancoAlt,
        topBar = {
            Surface(
                color = DHBranco,
                shadowElevation = 3.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            Icons.Default.ArrowBackIosNew,
                            contentDescription = "Voltar",
                            tint = DHTexto,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    Column {
                        Text(
                            "Meus Alimentos",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            color = DHTexto,
                            letterSpacing = (-0.3).sp
                        )
                        Text(
                            "Toque em um item para editar",
                            fontSize = 11.sp,
                            color = DHSub
                        )
                    }
                    Spacer(Modifier.weight(1f))
                    IconButton(onClick = { viewModel.carregarAlimentos() }) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Atualizar",
                            tint = DHVermelho,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
        }
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is AlimentosUiState.Loading -> EstadoCarregando()
                is AlimentosUiState.Error   -> EstadoErro(
                    mensagem = state.mensagem,
                    onRetry  = { viewModel.carregarAlimentos() }
                )
                is AlimentosUiState.Success -> {
                    if (state.alimentos.isEmpty()) {
                        EstadoVazio()
                    } else {
                        ListaAlimentos(
                            alimentos  = state.alimentos,
                            onItemClick = { viewModel.abrirEdicao(it) }
                        )
                    }
                }
            }

            // Dialog de edição
            if (alimentoSel != null) {
                DialogEdicaoAlimento(
                    alimento      = alimentoSel!!,
                    form          = edicaoForm,
                    edicaoState   = edicaoUiState,
                    onQntdChange      = viewModel::onQntdChange,
                    onValidadeChange  = viewModel::onValidadeChange,
                    onDescricaoChange = viewModel::onDescricaoChange,
                    onSalvar          = viewModel::salvarEdicao,
                    onDismiss         = viewModel::fecharEdicao
                )
            }
        }
    }
}

// ─────────────────────────────────────────────
// Lista de alimentos
// ─────────────────────────────────────────────
@Composable
private fun ListaAlimentos(
    alimentos: List<AlimentoDoadorUi>,
    onItemClick: (AlimentoDoadorUi) -> Unit
) {
    LazyColumn(
        contentPadding    = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            ResumoHeader(total = alimentos.size)
        }
        items(alimentos, key = { it.idAlimento }) { alimento ->
            CardAlimento(
                alimento    = alimento,
                onItemClick = onItemClick
            )
        }
        item { Spacer(Modifier.height(8.dp)) }
    }
}

@Composable
private fun ResumoHeader(total: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            "Cadastrados",
            fontSize = 13.sp,
            color = DHSub,
            fontWeight = FontWeight.Medium
        )
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = DHVermelho.copy(alpha = 0.10f)
        ) {
            Text(
                "$total ${if (total == 1) "item" else "itens"}",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = DHVermelho
            )
        }
    }
}


@Composable
private fun CardAlimento(
    alimento: AlimentoDoadorUi,
    onItemClick: (AlimentoDoadorUi) -> Unit
) {
    val icone  = categoriaIcone[alimento.categoria]  ?: Icons.Default.Fastfood
    val fundo  = categoriaCorFundo[alimento.categoria]  ?: DHVermelho.copy(alpha = 0.08f)
    val corIcon = categoriaCorIcone[alimento.categoria] ?: DHVermelho

    Card(
        shape     = RoundedCornerShape(18.dp),
        colors    = CardDefaults.cardColors(containerColor = DHBranco),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier  = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onItemClick(alimento) }
    ) {
        Row(
            modifier            = Modifier.padding(14.dp),
            verticalAlignment   = Alignment.CenterVertically
        ) {
            // Ícone de categoria
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(fundo),
                contentAlignment = Alignment.Center
            ) {
                Icon(icone, null, tint = corIcon, modifier = Modifier.size(26.dp))
            }

            Spacer(Modifier.width(14.dp))

            // Infos principais
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    alimento.nome,
                    fontWeight  = FontWeight.Bold,
                    fontSize    = 15.sp,
                    color       = DHTexto,
                    maxLines    = 1,
                    overflow    = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    alimento.categoria,
                    fontSize = 11.sp,
                    color    = DHSub,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ChipInfo(
                        icone = Icons.Default.Inventory2,
                        texto = "${alimento.qntd} un."
                    )
                    ChipInfo(
                        icone = Icons.Default.EventBusy,
                        texto = alimento.validade
                    )
                }
            }

            // Seta de edição
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(DHBrancoAlt),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint     = DHVermelho,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Descrição em faixa inferior suave
        if (alimento.descricao.isNotBlank()) {
            HorizontalDivider(color = DHCinza, modifier = Modifier.padding(horizontal = 14.dp))
            Text(
                alimento.descricao,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                fontSize = 12.sp,
                color    = DHSub,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
private fun ChipInfo(icone: ImageVector, texto: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = DHBrancoAlt
    ) {
        Row(
            modifier            = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
            verticalAlignment   = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icone, null, tint = DHSub, modifier = Modifier.size(11.dp))
            Text(texto, fontSize = 11.sp, color = DHSub, fontWeight = FontWeight.Medium)
        }
    }
}

// ─────────────────────────────────────────────
// Dialog de edição
// ─────────────────────────────────────────────
@Composable
private fun DialogEdicaoAlimento(
    alimento: AlimentoDoadorUi,
    form: com.example.foodcare.viewmodel.EdicaoAlimentoForm,
    edicaoState: EdicaoUiState,
    onQntdChange: (String) -> Unit,
    onValidadeChange: (String) -> Unit,
    onDescricaoChange: (String) -> Unit,
    onSalvar: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = { if (edicaoState !is EdicaoUiState.Salvando) onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier  = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            shape     = RoundedCornerShape(24.dp),
            color     = DHBranco,
            shadowElevation = 12.dp
        ) {
            Column(modifier = Modifier.padding(24.dp)) {

                // Cabeçalho do dialog
                Row(
                    modifier            = Modifier.fillMaxWidth(),
                    verticalAlignment   = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "Editar alimento",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize   = 18.sp,
                            color      = DHTexto
                        )
                        Spacer(Modifier.height(2.dp))
                        Text(
                            alimento.nome,
                            fontSize = 13.sp,
                            color    = DHSub,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    IconButton(
                        onClick  = onDismiss,
                        enabled  = edicaoState !is EdicaoUiState.Salvando,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DHBrancoAlt)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Fechar",
                            tint     = DHTexto,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                // Campos somente leitura
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    CampoLeitura(
                        label = "Nome",
                        valor = alimento.nome,
                        modifier = Modifier.weight(1.2f)
                    )
                    CampoLeitura(
                        label = "Categoria",
                        valor = alimento.categoria,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(Modifier.height(14.dp))
                HorizontalDivider(color = DHCinza)
                Spacer(Modifier.height(14.dp))

                // Campos editáveis
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    CampoEdicao(
                        label        = "Quantidade",
                        valor        = form.qntd,
                        onValorChange = onQntdChange,
                        icone        = Icons.Default.Inventory2,
                        teclado      = KeyboardType.Number,
                        modifier     = Modifier.weight(1f),
                        habilitado   = edicaoState !is EdicaoUiState.Salvando
                    )
                    CampoEdicao(
                        label        = "Validade",
                        valor        = form.validade,
                        onValorChange = onValidadeChange,
                        icone        = Icons.Default.EventBusy,
                        placeholder  = "AAAA-MM-DD",
                        modifier     = Modifier.weight(1f),
                        habilitado   = edicaoState !is EdicaoUiState.Salvando
                    )
                }

                Spacer(Modifier.height(10.dp))

                CampoEdicao(
                    label        = "Descrição",
                    valor        = form.descricao,
                    onValorChange = onDescricaoChange,
                    icone        = Icons.Default.Description,
                    singleLine   = false,
                    minLines     = 3,
                    modifier     = Modifier.fillMaxWidth(),
                    habilitado   = edicaoState !is EdicaoUiState.Salvando
                )

                // Mensagem de erro
                AnimatedVisibility(visible = edicaoState is EdicaoUiState.Erro) {
                    if (edicaoState is EdicaoUiState.Erro) {
                        Row(
                            modifier            = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFFFEBEE))
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment   = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.ErrorOutline,
                                null,
                                tint     = Color(0xFFC62828),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                edicaoState.mensagem,
                                fontSize = 12.sp,
                                color    = Color(0xFFC62828)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(18.dp))

                // Botões
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick  = onDismiss,
                        enabled  = edicaoState !is EdicaoUiState.Salvando,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape    = RoundedCornerShape(12.dp),
                        border   = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = Brush.linearGradient(listOf(DHCinza, DHCinza))
                        )
                    ) {
                        Text(
                            "Cancelar",
                            fontSize   = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color      = DHSub
                        )
                    }

                    Button(
                        onClick  = onSalvar,
                        enabled  = edicaoState !is EdicaoUiState.Salvando,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp),
                        shape    = RoundedCornerShape(12.dp),
                        colors   = ButtonDefaults.buttonColors(
                            containerColor = DHVermelho,
                            contentColor   = DHBranco
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
                    ) {
                        if (edicaoState is EdicaoUiState.Salvando) {
                            CircularProgressIndicator(
                                modifier  = Modifier.size(18.dp),
                                color     = DHBranco,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                Icons.Default.Check,
                                null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            Text(
                                "Salvar",
                                fontSize   = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

// ─────────────────────────────────────────────
// Componentes auxiliares
// ─────────────────────────────────────────────
@Composable
private fun CampoLeitura(
    label: String,
    valor: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            label,
            fontSize   = 10.sp,
            color      = DHSub,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp
        )
        Spacer(Modifier.height(4.dp))
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = DHBrancoAlt
        ) {
            Row(
                modifier          = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    Icons.Default.Lock,
                    null,
                    tint     = DHSub.copy(alpha = 0.5f),
                    modifier = Modifier.size(13.dp)
                )
                Text(
                    valor,
                    fontSize  = 13.sp,
                    color     = DHSub,
                    maxLines  = 1,
                    overflow  = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CampoEdicao(
    label: String,
    valor: String,
    onValorChange: (String) -> Unit,
    icone: ImageVector,
    teclado: KeyboardType = KeyboardType.Text,
    placeholder: String = "",
    singleLine: Boolean = true,
    minLines: Int = 1,
    habilitado: Boolean = true,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            label,
            fontSize   = 10.sp,
            color      = DHSub,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp
        )
        Spacer(Modifier.height(4.dp))
        OutlinedTextField(
            value         = valor,
            onValueChange = onValorChange,
            enabled       = habilitado,
            singleLine    = singleLine,
            minLines      = minLines,
            placeholder   = {
                Text(
                    placeholder.ifBlank { label },
                    fontSize = 13.sp,
                    color    = DHSub.copy(alpha = 0.4f)
                )
            },
            leadingIcon   = {
                Icon(icone, null, tint = DHVermelho, modifier = Modifier.size(17.dp))
            },
            keyboardOptions = KeyboardOptions(keyboardType = teclado),
            shape  = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = DHVermelho,
                unfocusedBorderColor = DHCinza,
                focusedTextColor     = DHTexto,
                unfocusedTextColor   = DHTexto,
                cursorColor          = DHVermelho,
                disabledBorderColor  = DHCinza,
                disabledTextColor    = DHSub
            ),
            textStyle = LocalTextStyle.current.copy(fontSize = 13.sp),
            modifier  = Modifier.fillMaxWidth()
        )
    }
}

// ─────────────────────────────────────────────
// Estados: loading, erro, vazio
// ─────────────────────────────────────────────
@Composable
private fun EstadoCarregando() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator(color = DHVermelho, strokeWidth = 3.dp)
        Spacer(Modifier.height(16.dp))
        Text("Carregando alimentos…", fontSize = 14.sp, color = DHSub)
    }
}

@Composable
private fun EstadoErro(mensagem: String, onRetry: () -> Unit) {
    Column(
        modifier            = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(DHVermelho.copy(alpha = 0.08f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.WifiOff,
                null,
                tint     = DHVermelho,
                modifier = Modifier.size(34.dp)
            )
        }
        Spacer(Modifier.height(16.dp))
        Text(
            mensagem,
            fontSize  = 14.sp,
            color     = DHSub,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = onRetry,
            shape   = RoundedCornerShape(12.dp),
            colors  = ButtonDefaults.buttonColors(containerColor = DHVermelho)
        ) {
            Icon(Icons.Default.Refresh, null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("Tentar novamente", fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun EstadoVazio() {
    Column(
        modifier            = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(DHVermelho.copy(alpha = 0.07f)),
            contentAlignment = Alignment.Center
        ) {
            Text("🥗", fontSize = 36.sp)
        }
        Spacer(Modifier.height(16.dp))
        Text(
            "Nenhum alimento cadastrado",
            fontWeight = FontWeight.Bold,
            fontSize   = 16.sp,
            color      = DHTexto
        )
        Spacer(Modifier.height(6.dp))
        Text(
            "Volte ao início e registre\nsua primeira doação.",
            fontSize  = 13.sp,
            color     = DHSub,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 19.sp
        )
    }
}
