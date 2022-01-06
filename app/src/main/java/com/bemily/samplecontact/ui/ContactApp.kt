package com.bemily.samplecontact.ui


import androidx.compose.runtime.Composable
import com.bemily.samplecontact.ui.theme.SampleContactTheme

@Composable
fun ContactApp() {
    SampleContactTheme {
        SystemUi {
            ContactScreen()
        }
    }
}