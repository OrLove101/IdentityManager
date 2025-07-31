package com.OrLove.peoplemanager.utils.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ProgressLoader(
    dotsCount: Int = 9,
    color: Color = Color.White,
    dotSize: Dp = 25.dp,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .alpha(0.75f)
            .background(Color.Gray)
            .pointerInput(Unit) { detectTapGestures {} },
    )
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        content = {
            Loader(
                dotsCount = dotsCount,
                color = color,
                dotSize = dotSize
            )
        }
    )
}

@Composable
fun Loader(
    dotsCount: Int = 9,
    color: Color,
    dotSize: Dp = 25.dp,
) {
    Box(
        modifier = Modifier
            .size(100.dp),
        contentAlignment = Alignment.Center,
    ) {
        for (i in 0 until dotsCount) {
            LoaderCircle(
                color = color,
                dotSize = dotSize,
                orderNumber = i,
                dotsCount = dotsCount
            )
        }
    }
}

@Composable
private fun LoaderCircle(
    color: Color,
    dotSize: Dp,
    orderNumber: Int,
    dotsCount: Int
) {
    val delayUnit = 200
    val delay = delayUnit * orderNumber
    val animation = rememberInfiniteTransition(label = "loaderCircleAnimation")
    val rotation by animation.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "loaderCircleRotation"
    )
    val duration = delayUnit * dotsCount
    val scale by animation.animateFloat(
        initialValue = 0f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = keyframes {
                durationMillis = duration + delayUnit * 2
                0f at delay with LinearEasing
                1f at delay + delayUnit with LinearEasing
                0f at delay + duration with LinearEasing
            }
        ),
        label = "loaderCircleScale"
    )
    Box(
        modifier = Modifier
            .rotate(degrees = orderNumber * (360f / dotsCount))
            .rotate(degrees = rotation)
            .width(width = 90.dp)
    ) {
        Box(
            modifier = Modifier
                .size(dotSize)
                .scale(scale)
                .background(color = color, shape = CircleShape)
        )
    }
}