package com.example.foodcare.view

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodcare.R
import com.example.foodcare.ui.theme.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.foodcare.viewmodel.FinalidadeViewModel
import kotlinx.coroutines.delay

@Composable
fun TelaFinalidade(
    onAvancar: () -> Unit = {},
    viewModel: FinalidadeViewModel = viewModel()
) {
    var visivel by remember { mutableStateOf(false) }
    val slideY by animateFloatAsState(
        targetValue = if (visivel) 0f else 60f,
        animationSpec = tween(durationMillis = 700, easing = EaseOutBack),
        label = "slideY"
    )

    LaunchedEffect(Unit) {
        delay(200)
        visivel = true
    }

    LaunchedEffect(viewModel.finalidadeSucesso) {
        if (viewModel.finalidadeSucesso) {
            onAvancar()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrancoAlt),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.52f)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(vermClaro, vermEscuro)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.05f),
                    radius = size.width * 0.7f,
                    center = Offset(size.width * 0.85f, size.height * 0.1f)
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.05f),
                    radius = size.width * 0.5f,
                    center = Offset(size.width * 0.1f, size.height * 0.9f)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    Text(
                        text = "FoodCare ",
                        color = Branco,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Image(
                        painter = painterResource(id = R.drawable.logofoodcarebranca),
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }

                LogoCesta(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .aspectRatio(1f)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.48f)
                .padding(horizontal = 32.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.offset(y = slideY.dp)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Com qual finalidade você utilizará esse aplicativo?",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = textoEscuro,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 32.dp)
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        FoodCareButton(
                            text = "Doar Alimentos",
                            onClick = {
                                viewModel.tipoUsuario = "doador"
                                viewModel.salvarFinalidade()
                            },
                            backgroundColor = Vermelho,
                            textColor = Branco
                        )
                        FoodCareButton(
                            text = "Receber Doações",
                            onClick = {
                                viewModel.tipoUsuario = "receptor"
                                viewModel.salvarFinalidade()
                            },
                            backgroundColor = Vermelho,
                            textColor = Branco
                        )
                        FoodCareButton(
                            text = "Doar e Receber",
                            onClick = {
                                viewModel.tipoUsuario = "ambos"
                                viewModel.salvarFinalidade()
                            },
                            backgroundColor = Vermelho,
                            textColor = Branco
                        )
                    }

                    if (viewModel.mensagemErro.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = viewModel.mensagemErro,
                            color = Color.Red,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun LogoCesta(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.imagemcesta),
        contentDescription = "Cesta de Alimentos",
        modifier = modifier
    )
}