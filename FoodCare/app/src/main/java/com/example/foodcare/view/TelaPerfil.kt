package com.example.foodcare.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodcare.data.api.SessaoUsuario
import com.example.foodcare.ui.theme.*
import com.example.foodcare.viewmodel.PerfilViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaPerfil(
    onVoltar: () -> Unit       = {},
    onEditarPerfil: () -> Unit = {},
    onLogout: () -> Unit       = {},
    viewModel : PerfilViewModel = viewModel()
) {
    val ehDoador      = SessaoUsuario.tipoUsuario == "doador" || SessaoUsuario.tipoUsuario == "ambos"
    val tipoLabel     = if (ehDoador) "Doador" else "Receptor"
    val tipoEmoji     = if (ehDoador) "🤝" else "🙏"
    val tipoColor     = if (ehDoador) PVerde else PAzul
    val tipoBg        = if (ehDoador) PVerdeFundo else PAzulFundo
    val tipoIcone     = if (ehDoador) Icons.Default.VolunteerActivism
    else          Icons.Default.CardGiftcard
    val tipoMensagem  = if (ehDoador) "Obrigado por contribuir com a comunidade! 💚"
    else          "Conectado com doadores próximos a você 💙"
    val docLabel      = if (viewModel.tipoPessoa == "PJ") "CNPJ" else "CPF"

    LaunchedEffect(Unit) {
        viewModel.carregarPerfil()
    }

    Scaffold(
        containerColor = PFundo,
        topBar = {
            TopAppBar(
                title = {
                    Text("Meu Perfil", fontWeight = FontWeight.Bold,
                        fontSize = 18.sp, color = PBranco)
                },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar", tint = PBranco)
                    }
                },
                actions = {
                    IconButton(onClick = onEditarPerfil) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = PBranco)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PVermelho)
            )
        }
    ) { pad ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(pad)
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(listOf(PVermelhoClr, PVermelhoEsc))
                    )
                    .padding(vertical = 32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {


                    Box(
                        modifier = Modifier
                            .size(88.dp)
                            .clip(CircleShape)
                            .background(PBranco.copy(alpha = 0.2f))
                            .border(3.dp, PBranco, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = viewModel.nome.take(1).uppercase(),
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = PBranco
                        )
                    }

                    Spacer(Modifier.height(12.dp))
                    Text(viewModel.nome, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = PBranco)
                    Spacer(Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = PBranco.copy(alpha = 0.2f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(tipoEmoji, fontSize = 14.sp)
                            Text(tipoLabel, fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold, color = PBranco)
                            Text("·", color = PBranco.copy(alpha = 0.6f))
                            Text(
                                if (viewModel.tipoPessoa == "PJ") "Pessoa Jurídica" else "Pessoa Física",
                                fontSize = 13.sp, color = PBranco.copy(alpha = 0.85f)
                            )
                        }
                    }


                    if (ehDoador) {
                        Spacer(Modifier.height(12.dp))
                        EstrelasInline(pontuacao = viewModel.pontuacao, corTexto = PBranco)
                    }
                }
            }

            //exibe mensagem de erro
            if (viewModel.mensagemErro != null) {
                AlertDialog(
                    onDismissRequest = {
                        viewModel.mensagemErro = null
                    },
                    title = {
                        Text("Erro")
                    },
                    text = {
                        Text(viewModel.mensagemErro!!)
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                viewModel.mensagemErro = null
                            }
                        ) {
                            Text("OK")
                        }
                    }
                )
            }


            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {


                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = tipoBg,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Icon(tipoIcone, null, tint = tipoColor,
                            modifier = Modifier.size(26.dp))
                        Column {
                            Text("Você é um $tipoLabel",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp, color = tipoColor)
                            Text(tipoMensagem,
                                fontSize = 12.sp,
                                color = tipoColor.copy(alpha = 0.75f))
                        }
                    }
                }


                CardSecao("Dados Pessoais", Icons.Default.Person) {
                    LinhaDetalhe(Icons.Default.Badge,         "Nome",     viewModel.nome, viewModel.modoEdicao,onValueChange = { viewModel.nome = it })
                    LinhaDetalhe(Icons.Default.Email,         "E-mail",   viewModel.email, viewModel.modoEdicao,onValueChange = { viewModel.email = it })
                    LinhaDetalhe(Icons.Default.AssignmentInd, docLabel,   viewModel.documento, false,onValueChange = { })
                }

                CardSecao("Endereço", Icons.Default.LocationOn) {
                    LinhaDetalhe(Icons.Default.Home,
                        "Rua", viewModel.rua, viewModel.modoEdicao,onValueChange = { viewModel.rua = it })
                    LinhaDetalhe(Icons.Default.Pin, "Número", viewModel.numero, viewModel.modoEdicao, { viewModel.numero = it })
                    LinhaDetalhe(Icons.Default.Map,              "Bairro",  viewModel.bairro, viewModel.modoEdicao,onValueChange = { viewModel.bairro = it })
                    LinhaDetalhe(Icons.Default.LocationCity,     "Cidade",  viewModel.cidade, viewModel.modoEdicao,onValueChange = { viewModel.cidade = it })
                    LinhaDetalhe(Icons.Default.MarkunreadMailbox,"CEP",     viewModel.cep, viewModel.modoEdicao,onValueChange = { viewModel.cep = it })
                }


                if (ehDoador) {
                    CardSecao("Meu Desempenho", Icons.Default.Star) {
                        CardNota(viewModel.pontuacao)
                    }
                }

                Spacer(Modifier.height(4.dp))

                Column{
                    if (viewModel.mensagemSucesso) {
                        Text(
                            text = "Alterações realizadas com sucesso.",
                            color = Color.Green
                        )
                    }
                }

                Spacer(Modifier.height(4.dp))

                OutlinedButton(
                    onClick = {
                        viewModel.modoEdicao = !viewModel.modoEdicao
                        if(!viewModel.modoEdicao){
                            viewModel.salvarPerfil()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, PVermelho),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PVermelho)
                ) {
                    Icon(Icons.Default.Edit, null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if(viewModel.modoEdicao)"Salvar Alterações" else "Editar Perfil",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp)

                }

                Button(
                    onClick = {
                        viewModel.logout()
                        onLogout()
                       },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFFFF0F0),
                        contentColor   = PVermelho
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Icon(Icons.AutoMirrored.Filled.Logout, null,
                        modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Sair da conta", fontWeight = FontWeight.SemiBold, fontSize = 15.sp, )
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}



@Composable
internal fun CardSecao(
    titulo: String,
    icone: ImageVector,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape     = RoundedCornerShape(16.dp),
        colors    = CardDefaults.cardColors(containerColor = Color(0xFFFFFFFF)),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier  = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 10.dp)
            ) {
                Icon(icone, null, tint = Color(0xFFD32F2F),
                    modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(8.dp))
                Text(titulo, fontWeight = FontWeight.Bold,
                    fontSize = 15.sp, color = Color(0xFF1C1C1C))
            }
            HorizontalDivider(color = Color(0xFFEEEEEE))
            Spacer(Modifier.height(10.dp))
            content()
        }
    }
}

@Composable
internal fun LinhaDetalhe(
    icone: ImageVector,
    label: String,
    valor: String,
    modoEdicao: Boolean,
    onValueChange: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icone, null, tint = Color(0xFF757575),
            modifier = Modifier.size(17.dp))
        Spacer(Modifier.width(10.dp))
        Column {
            Text(label, fontSize = 11.sp, color = Color(0xFF757575))

            if(modoEdicao){
                FoodCareTextField(valor, onValueChange)
            }
            else{
                Text(
                    if (valor.isBlank()) "—" else valor,
                    fontSize = 14.sp,
                    color = Color(0xFF1C1C1C),
                    fontWeight = FontWeight.Medium
                )
            }

        }
    }
}

@Composable
internal fun EstrelasInline(pontuacao: Double?, corTexto: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        val nota = pontuacao ?: 0.0
        repeat(5) { i ->
            Icon(
                if (i < nota.toInt()) Icons.Default.Star else Icons.Default.StarBorder,
                null, tint = Color(0xFFFFC107),
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.width(4.dp))
        Text(
            if (pontuacao != null) String.format("%.1f", pontuacao) else "—",
            fontSize = 13.sp, color = corTexto, fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
internal fun CardNota(pontuacao: Double?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFFFFF8E1))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Avaliação dos receptores",
                fontSize = 12.sp, color = Color(0xFF757575))
            Spacer(Modifier.height(6.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                val nota = pontuacao ?: 0.0
                repeat(5) { i ->
                    Icon(
                        if (i < nota.toInt()) Icons.Default.Star else Icons.Default.StarBorder,
                        null, tint = Color(0xFFFFA000),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        }
        Text(
            if (pontuacao != null) String.format("%.1f", pontuacao) else "—",
            fontSize = 32.sp, fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFFFA000)
        )
    }
}