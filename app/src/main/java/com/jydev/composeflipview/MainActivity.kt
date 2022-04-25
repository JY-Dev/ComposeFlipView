package com.jydev.composeflipview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.layout.Layout

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var expanded by remember {
                mutableStateOf(false)
            }
            Column {
                Button(onClick = { expanded = !expanded }) {
                    Text(text = "버튼")
                }
                FlipView(
                    expanded = expanded, spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ) {
                    Text(text = "테스트")
                    Text(text = "테스트")
                    Text(text = "테스트")
                    Text(text = "테스트")
                    Text(text = "테스트")
                    Text(text = "테스트")
                    Text(text = "테스트")
                    Text(text = "테스트")
                    Text(text = "테스트")
                }
            }

        }
    }
}

@Composable
fun FlipView(
    expanded: Boolean,
    animationSpec: AnimationSpec<Int>,
    content: @Composable () -> Unit
) {
    var height by remember { mutableStateOf(0) }
    val heightAnimation by animateIntAsState(
        if (expanded) height else 0,
        animationSpec
    )
    Layout(content = content, measurePolicy = { measurables, constraints ->
        var measureHeight = 0
        var measureWidth = 0
        val placeable = measurables.map { measurable ->
            val placeable = measurable.measure(constraints = constraints)
            measureHeight += placeable.height
            measureWidth = Math.max(placeable.width, measureWidth)
            placeable
        }
        height = measureHeight
        layout(width = measureWidth, height = heightAnimation) {
            var yPosition = 0
            placeable.forEach {
                it.placeRelativeWithLayer(0, yPosition)
                yPosition += it.height
            }
        }
    })
}