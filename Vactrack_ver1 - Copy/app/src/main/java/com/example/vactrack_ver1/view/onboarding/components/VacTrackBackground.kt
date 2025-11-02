package com.example.vactrack_ver1.view.onboarding.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.example.vactrack_ver1.design.BrandPalette

@Composable
fun VacTrackBackground(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.fillMaxSize()) {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.White,
                    BrandPalette.MistWhite,
                    Color.White
                ),
                startY = 0f,
                endY = size.height
            )
        )

        val pathColor = BrandPalette.OceanBlue.copy(alpha = 0.18f)
        val strokeWidth = 2.dp.toPx()
        val dashPath = PathEffect.dashPathEffect(floatArrayOf(24f, 18f), 0f)
        val columns = 3
        val columnSpacing = size.width / (columns + 1)

        repeat(columns) { column ->
            val x = columnSpacing * (column + 1)
            val path = Path().apply {
                moveTo(x, 0f)
                cubicTo(x, size.height * 0.3f, x - columnSpacing / 2f, size.height * 0.6f, x, size.height)
            }
            drawPath(
                path = path,
                color = pathColor,
                style = Stroke(width = strokeWidth, pathEffect = dashPath)
            )
        }

        val nodeColor = BrandPalette.OceanBlue.copy(alpha = 0.35f)
        val outerRadius = 11.dp.toPx()
        val innerRadius = 4.dp.toPx()
        val positions = listOf(
            Offset(columnSpacing * 0.8f, size.height * 0.12f),
            Offset(columnSpacing * 1.4f, size.height * 0.28f),
            Offset(columnSpacing * 2.2f, size.height * 0.18f),
            Offset(columnSpacing * 0.6f, size.height * 0.55f),
            Offset(columnSpacing * 1.8f, size.height * 0.68f),
            Offset(columnSpacing * 2.4f, size.height * 0.82f),
            Offset(columnSpacing * 1.1f, size.height * 0.88f)
        )

        positions.forEach { center ->
            drawCircle(color = nodeColor, radius = outerRadius, center = center)
            drawCircle(color = Color.White, radius = innerRadius, center = center)
        }
    }
}
