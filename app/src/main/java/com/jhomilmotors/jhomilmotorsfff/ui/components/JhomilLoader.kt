package com.jhomilmotors.jhomilmotorsfff.ui.components // O tu paquete correspondiente

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import com.jhomilmotors.jhomilmotorsfff.R

@Composable
fun JhomilLoader(
    modifier: Modifier = Modifier,
    animResId: Int = R.raw.wheel_loading
) {
    // 1. Cargamos la composición
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(animResId))

    // 2. Controlamos la animación (infinita)
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )

    // 3. Pintamos la animación
    LottieAnimation(
        composition = composition,
        progress = { progress },
        modifier = modifier.size(150.dp)
    )
}