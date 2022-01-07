package com.bemily.samplecontact.ui

import android.Manifest
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bemily.samplecontact.R
import com.bemily.samplecontact.ui.component.SurfaceTopAppBar
import com.bemily.samplecontact.ui.theme.SampleContactTheme
import com.bemily.samplecontact.util.ContactHelper
import com.google.accompanist.insets.navigationBarsPadding
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionRequired
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ContactScreen(modifier: Modifier = Modifier) {
    val lazyListState = rememberLazyListState()
    val contactPermissionState = rememberPermissionState(Manifest.permission.READ_CONTACTS)
    val contactList = ContactHelper(LocalContext.current).getContacts()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            SurfaceTopAppBar(
                title = {
                    Row {
                        Text(text = stringResource(R.string.app_name))
                        Text(text = " (${contactList.size})")
                    }
                },
                navigationIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_launcher_foreground),
                        contentDescription = null,
                        tint = MaterialTheme.colors.onSurface
                    )
                }
            )
        }
    ) {
        PermissionRequired(
            permissionState = contactPermissionState,
            permissionNotGrantedContent = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(onClick = { contactPermissionState.launchPermissionRequest() }) {
                        Text(text = "Request Permission")
                    }
                }
            },
            permissionNotAvailableContent = { /* TODO */ },
            content = {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    state = lazyListState
                ) {
                    items(contactList) { item ->
                        ContactItem(
                            name = item.name,
                            phoneNumbers = item.phoneNumbers
                        )
                        Divider()
                    }
                    item {
                        Spacer(modifier = Modifier.navigationBarsPadding())
                    }
                }

            }
        )
    }
}

@Composable
fun ContactItem(
    name: String = "",
    phoneNumbers: List<String> = listOf()
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onSurface
        )
        Text(
            text = when (phoneNumbers.size == 1) {
                true -> phoneNumbers.first()
                else -> stringResource(R.string.contact_item_phone_number_count, phoneNumbers.first(), phoneNumbers.size - 1)
            },
            style = MaterialTheme.typography.subtitle1,
            color = MaterialTheme.colors.onSurface.copy(alpha = .5f)
        )
    }
}

@Preview("light theme", uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview("dark theme", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ContactAppPreview() {
    SampleContactTheme {
        ContactScreen()
    }
}