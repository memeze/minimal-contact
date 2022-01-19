package com.minimal.contact.ui.component

import androidx.compose.material.Surface
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.statusBarsPadding

@Composable
fun SurfaceTopAppBar(
    modifier: Modifier = Modifier,
    navigationIcon: @Composable (() -> Unit)? = null,
    title: @Composable () -> Unit
) {
    Surface(elevation = 4.dp) {
        TopAppBar(
            modifier = modifier.statusBarsPadding(),
            backgroundColor = Color.Transparent,
            elevation = 0.dp,
            navigationIcon = navigationIcon,
            title = title
        )
    }
}