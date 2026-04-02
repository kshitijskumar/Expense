package org.example.project.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.example.project.ui.theme.AppColors
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow

data class BarEntry(val label: String, val value: Float)

@Composable
fun HorizontalBarChart(
    entries: List<BarEntry>,
    modifier: Modifier = Modifier,
    labelBarGap: Dp = 8.dp,
    barValueGap: Dp = 8.dp,
    valueFormatter: (Float) -> String = { it.toInt().toString() }
) {
    if (entries.isEmpty()) return

    val colors = AppColors.current
    val maxValue = entries.maxOf { it.value }
    val (niceMax, divisions) = computeDivisions(maxValue)

    val textMeasurer = rememberTextMeasurer()
    val labelStyle = MaterialTheme.typography.labelSmall
    val density = LocalDensity.current

    var triggered by remember { mutableStateOf(false) }
    val progress by animateFloatAsState(
        targetValue = if (triggered) 1f else 0f,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing)
    )
    LaunchedEffect(Unit) { triggered = true }

    BoxWithConstraints(modifier = modifier) {
        val maxLabelNaturalWidth = with(density) {
            entries.maxOf { entry ->
                textMeasurer.measure(text = entry.label, style = labelStyle).size.width
            }.toDp()
        }
        val labelColWidth = minOf(maxLabelNaturalWidth, maxWidth * 0.20f)

        val valueColWidth = with(density) {
            entries.maxOf { entry ->
                textMeasurer.measure(text = valueFormatter(entry.value), style = labelStyle).size.width
            }.toDp()
        }

        Column {
            entries.forEach { entry ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = entry.label,
                        style = labelStyle,
                        color = colors.textPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.width(labelColWidth)
                    )

                    Spacer(Modifier.width(labelBarGap))

                    val barFraction = (entry.value / niceMax) * progress
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(20.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(barFraction)
                                .fillMaxHeight()
                                .background(
                                    color = colors.primary,
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                    }

                    Spacer(Modifier.width(barValueGap))

                    Text(
                        text = valueFormatter(entry.value),
                        style = labelStyle,
                        color = colors.textSecondary,
                        textAlign = TextAlign.End,
                        modifier = Modifier.width(valueColWidth)
                    )
                }

                Spacer(Modifier.height(12.dp))
            }
        }
    }
}

private fun computeDivisions(maxValue: Float, targetCount: Int = 5): Pair<Float, List<Float>> {
    if (maxValue <= 0f) return Pair(1f, listOf(0f, 1f))

    val maxD = maxValue.toDouble()
    val rawStep = maxD / targetCount
    val magnitude = 10.0.pow(floor(log10(rawStep)))
    val niceStep = when {
        rawStep / magnitude <= 1.0 -> magnitude
        rawStep / magnitude <= 2.0 -> 2.0 * magnitude
        rawStep / magnitude <= 5.0 -> 5.0 * magnitude
        else -> 10.0 * magnitude
    }
    val niceMax = ceil(maxD / niceStep) * niceStep
    val divisions = generateSequence(0.0) { it + niceStep }
        .takeWhile { it <= niceMax + niceStep * 0.001 }
        .map { it.toFloat() }
        .toList()
    return Pair(niceMax.toFloat(), divisions)
}
