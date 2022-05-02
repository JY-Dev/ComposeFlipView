package com.jydev.composeflipview

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Column {
                listOf(
                    "안녕",
                    "고라니",
                    "사랑",
                    "행복"
                ).forEach {
                    Test(it)
                }

            }

        }
    }
}

@Composable
fun Test(title : String){
    var expanded by remember {
        mutableStateOf(false)
    }
    val rotate: Float by animateFloatAsState(if (expanded) 180f else 0f)
    Row {
        Text(
            text = title,
            fontSize = 18.sp,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(24.dp))
        IconButton(
            modifier = Modifier.rotate(rotate),
            onClick = {expanded = !expanded}
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_frequently_asked_questions),
                contentDescription = null
            )
        }
    }

    FlipView(expanded = expanded, animationSpec = spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessLow
    )) {
        Text(text = "컨텐츠 입니다.",fontSize =20.sp)
        Text(text = "컨텐츠 입니다.",fontSize =20.sp)
        Text(text = "컨텐츠 입니다.",fontSize =20.sp)
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
    var isMeasure by remember {
        mutableStateOf(false)
    }
    Box(modifier = if(isMeasure) Modifier.height(with(LocalDensity.current) { heightAnimation.toDp() }) else Modifier) {
        Layout(content = content, measurePolicy = { measurables, constraints ->
            var measureHeight = 0
            var measureWidth = 0
            val placeable = measurables.map { measurable ->
                val placeable = measurable.measure(constraints = constraints)
                measureHeight += placeable.height
                measureWidth = Math.max(placeable.width, measureWidth)
                placeable
            }
            if(measureHeight != 0){
                height = measureHeight
                isMeasure = true
            }
            println("test : $measureHeight")
            layout(width = measureWidth, height = heightAnimation) {
                var yPosition = 0
                placeable.forEach {
                    it.placeRelativeWithLayer(0, yPosition)
                    yPosition += it.height
                }
            }
        })
    }

}