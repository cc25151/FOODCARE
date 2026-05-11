package com.example.foodcare.view

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.ui.geometry.Offset
import kotlinx.coroutines.delay
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.foodcare.R
import com.example.foodcare.ui.theme.*



@Composable
fun TelaInicial(
    onEntrarClick: () -> Unit = {},
    onCadastrarClick: () -> Unit = {},
    onQuemSomosClick: () -> Unit ={}
) {
    var visivel by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (visivel) 1f else 0f,
        animationSpec = tween(durationMillis = 800, easing = EaseInOutCubic),
        label = "alpha"
    )
    val slideY by animateFloatAsState(
        targetValue = if (visivel) 0f else 60f,
        animationSpec = tween(durationMillis = 700, easing = EaseOutBack),
        label = "slideY"
    )

    LaunchedEffect(Unit) {
        delay(200)
        visivel = true
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

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "FoodCare",
                    color = Branco,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                LogoBranca(
                    modifier = Modifier
                        .size(200.dp)
                        .padding(8.dp)
                )
            }
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.48f)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(modifier = Modifier.offset(y = slideY.dp)) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 36.dp)
                ) {
                    Row {
                        Text("Bem vindo ao ")

                        Text(
                            "FOODCARE",
                            color = FoodCareBlue,
                            fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.clickable { onQuemSomosClick() }
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Conectando quem tem com quem precisa",
                        fontSize = 13.sp,
                        color = Color(0xFF888888),
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.2.sp
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = slideY.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                FoodCareButton(
                    text = "Entrar",
                    onClick = onEntrarClick,
                    backgroundColor = Vermelho,
                    textColor = Branco
                )
                FoodCareButton(
                    text = "Cadastrar",
                    onClick = onCadastrarClick,
                    backgroundColor = Vermelho,
                    textColor = Branco
                )
            }
        }
    }
}


@Composable
fun FoodCareButton(
    text: String,
    onClick: () -> Unit,
    backgroundColor: Color = Vermelho,
    textColor: Color = Branco,
    outlined: Boolean = false
) {
    var pressionado by remember { mutableStateOf(false) }
    val escala by animateFloatAsState(
        targetValue = if (pressionado) 0.97f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "btnScale"
    )

    if (outlined) {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .scale(escala),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Vermelho),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Vermelho)
        ) {
            Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.5.sp)
        }
    } else {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .scale(escala)
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp)),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = backgroundColor,
                contentColor = textColor
            ),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        ) {
            Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.5.sp)
        }
    }
}


@Composable
fun LogoBranca(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logofoodcarebranca),
        contentDescription = "Logo FoodCare",
        modifier = modifier
    )

}

@Composable
fun LogoPreta(modifier: Modifier = Modifier){
    Image(
        painter = painterResource(id = R.drawable.logofoodcarepreta),
        contentDescription = "Logo FoodCare",
        modifier = modifier
    )
}