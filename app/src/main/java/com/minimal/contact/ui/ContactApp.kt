package com.minimal.contact.ui


import androidx.compose.runtime.Composable
import com.minimal.contact.ui.theme.MinimalContactTheme

@Composable
fun ContactApp() {
    MinimalContactTheme {
        SystemUi {
            ContactScreen()
        }
    }
}