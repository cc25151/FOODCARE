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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.translate
import kotlinx.coroutines.delay


val FoodCareRed       = Color(0xFFCC1E1E)
val FoodCareDarkRed   = Color(0xFFAA1515)
val FoodCareLightRed  = Color(0xFFE53935)
val FoodCareBlue      = Color(0xFF1565C0)
val FoodCareWhite     = Color(0xFFFFFFFF)
val FoodCareOffWhite  = Color(0xFFFAFAFA)
val FoodCareTextDark  = Color(0xFF1A1A2E)
val FoodCareGray      = Color(0xFFEEEEEE)

@Composable
fun TelaInicial(
    onEntrarClick: () -> Unit = {},
    onCadastrarClick: () -> Unit = {}
) {
    var visible by remember { mutableStateOf(false) }
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(durationMillis = 800, easing = EaseInOutCubic),
        label = "alpha"
    )
    val slideY by animateFloatAsState(
        targetValue = if (visible) 0f else 60f,
        animationSpec = tween(durationMillis = 700, easing = EaseOutBack),
        label = "slideY"
    )

    LaunchedEffect(Unit) {
        delay(200)
        visible = true
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FoodCareOffWhite),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.52f)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(FoodCareLightRed, FoodCareDarkRed)
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
                    color = FoodCareWhite,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                FoodCareIllustration(
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
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(color = FoodCareTextDark, fontWeight = FontWeight.Bold)) {
                                append("Bem vindo ao ")
                            }
                            withStyle(SpanStyle(color = FoodCareBlue, fontWeight = FontWeight.ExtraBold)) {
                                append("FOODCARE")
                            }
                        },
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        letterSpacing = 0.3.sp
                    )
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
                    backgroundColor = FoodCareRed,
                    textColor = FoodCareWhite
                )
                FoodCareButton(
                    text = "Cadastrar",
                    onClick = onCadastrarClick,
                    backgroundColor = FoodCareRed,
                    textColor = FoodCareWhite
                )
            }
        }
    }
}


@Composable
fun FoodCareButton(
    text: String,
    onClick: () -> Unit,
    backgroundColor: Color = FoodCareRed,
    textColor: Color = FoodCareWhite,
    outlined: Boolean = false
) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.97f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "btnScale"
    )

    if (outlined) {
        OutlinedButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .scale(scale),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = FoodCareRed),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, FoodCareRed)
        ) {
            Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, letterSpacing = 0.5.sp)
        }
    } else {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .scale(scale)
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
fun FoodCareIllustration(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val heartScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.12f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "heartScale"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val strokeW = w * 0.035f

        val handPath = Path().apply {
            moveTo(w * 0.18f, h * 0.82f)
            cubicTo(w * 0.10f, h * 0.75f, w * 0.08f, h * 0.60f, w * 0.20f, h * 0.58f)
            cubicTo(w * 0.30f, h * 0.55f, w * 0.34f, h * 0.62f, w * 0.38f, h * 0.62f)
            lineTo(w * 0.62f, h * 0.62f)
            cubicTo(w * 0.72f, h * 0.62f, w * 0.76f, h * 0.68f, w * 0.74f, h * 0.76f)
            cubicTo(w * 0.70f, h * 0.85f, w * 0.55f, h * 0.88f, w * 0.42f, h * 0.88f)
            close()
        }
        drawPath(handPath, color = Color.White, style = Stroke(width = strokeW, cap = StrokeCap.Round))

        val breadLeft   = w * 0.08f
        val breadTop    = h * 0.30f
        val breadRight  = w * 0.40f
        val breadBottom = h * 0.58f
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(breadLeft, breadTop),
            size = Size(breadRight - breadLeft, breadBottom - breadTop),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.06f),
            style = Stroke(width = strokeW, cap = StrokeCap.Round)
        )
        drawLine(Color.White,
            Offset(breadLeft + (breadRight - breadLeft) * 0.35f, breadTop + 8f),
            Offset(breadLeft + (breadRight - breadLeft) * 0.35f, breadBottom - 8f),
            strokeW * 0.7f)

        val cartonLeft   = w * 0.60f
        val cartonTop    = h * 0.20f
        val cartonRight  = w * 0.88f
        val cartonBottom = h * 0.60f
        drawRoundRect(
            color = Color.White,
            topLeft = Offset(cartonLeft, cartonTop + h * 0.10f),
            size = Size(cartonRight - cartonLeft, cartonBottom - cartonTop - h * 0.10f),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.04f),
            style = Stroke(width = strokeW, cap = StrokeCap.Round)
        )
        val roofPath = Path().apply {
            moveTo(cartonLeft, cartonTop + h * 0.10f)
            lineTo((cartonLeft + cartonRight) / 2f, cartonTop)
            lineTo(cartonRight, cartonTop + h * 0.10f)
        }
        drawPath(roofPath, color = Color.White, style = Stroke(width = strokeW, cap = StrokeCap.Round))

        val hx = w * 0.50f
        val hy = h * 0.48f
        val hr = w * 0.12f * heartScale
        translate(hx - hr, hy - hr) {
            val heartPath = Path().apply {
                val hw = hr * 2f
                moveTo(hw / 2f, hw * 0.75f)
                cubicTo(0f, hw * 0.45f, 0f, hw * 0.10f, hw / 4f, hw * 0.10f)
                cubicTo(hw * 0.375f, hw * 0.10f, hw / 2f, hw * 0.30f, hw / 2f, hw * 0.30f)
                cubicTo(hw / 2f, hw * 0.30f, hw * 0.625f, hw * 0.10f, hw * 0.75f, hw * 0.10f)
                cubicTo(hw, hw * 0.10f, hw, hw * 0.45f, hw / 2f, hw * 0.75f)
                close()
            }
            drawPath(heartPath, color = Color.White)
        }
    }
}