package com.minimal.contact.util

import android.content.ContentResolver
import android.database.ContentObserver
import android.database.Cursor
import android.net.Uri
import android.os.Handler
import android.os.Looper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart


internal fun ContentResolver.query(
    uri: Uri,
    projection: Array<String>? = null,
    selection: String? = null,
    selectionArgs: Array<String>? = null,
    sortOrder: String? = null
): Cursor? {
    return query(uri, projection, selection, selectionArgs, sortOrder)
}

internal fun ContentResolver.queryFlow(
    uri: Uri,
    projection: Array<String>? = null,
    selection: String? = null,
    selectionArgs: Array<String>? = null,
    sortOrder: String? = null
): Flow<Cursor?> {
    return callbackFlow(uri)
        .onStart { emit(Unit) }
        .map { query(uri, projection, selection, selectionArgs, sortOrder) }
}

@OptIn(ExperimentalCoroutinesApi::class)
internal fun ContentResolver.callbackFlow(uri: Uri): Flow<Unit> {
    return callbackFlow {
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                if (channel.isClosedForSend.not()) {
                    trySend(Unit)
                }
            }
        }
        registerContentObserver(uri, true, observer)
        awaitClose {
            unregisterContentObserver(observer)
        }
    }
}