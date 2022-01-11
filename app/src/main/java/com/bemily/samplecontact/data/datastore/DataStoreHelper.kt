package com.bemily.samplecontact.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.bemily.samplecontact.data.datastore.DataStoreHelper.Companion.DATA_STORE_NAME
import com.bemily.samplecontact.data.datastore.DataStoreHelper.PreferencesKeys.CONTACT_LAST_UPDATE_TIME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_NAME)

@Singleton
class DataStoreHelper @Inject constructor(
    @ApplicationContext val context: Context
) {
    fun getContactLastUpdateTime() = context.dataStore.data.map { pref ->
        pref[CONTACT_LAST_UPDATE_TIME] ?: 0L
    }

    suspend fun setContactLastUpdateTime(value: Long) {
        context.dataStore.edit { pref ->
            pref[CONTACT_LAST_UPDATE_TIME] = value
        }
    }

    suspend fun clear() {
        context.dataStore.edit { pref ->
            pref.clear()
        }
    }

    private object PreferencesKeys {
        val CONTACT_LAST_UPDATE_TIME = longPreferencesKey(KEY_CONTACT_LAST_UPDATE_TIME)
    }

    companion object {
        const val DATA_STORE_NAME = "contact"
        private const val KEY_CONTACT_LAST_UPDATE_TIME = "KEY_CONTACT_LAST_UPDATE_TIME"
    }
}