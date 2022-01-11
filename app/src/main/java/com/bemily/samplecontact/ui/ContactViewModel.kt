package com.bemily.samplecontact.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bemily.samplecontact.data.datastore.DataStoreHelper
import com.bemily.samplecontact.data.model.Contact
import com.bemily.samplecontact.data.model.Result
import com.bemily.samplecontact.util.ContactHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val dataStoreHelper: DataStoreHelper,
    private val contactHelper: ContactHelper
) : ViewModel() {

    private val _contactList = MutableStateFlow<List<Contact>>(emptyList())
    var contactList = _contactList

    private val lastUpdatedTime: StateFlow<Long> =
        dataStoreHelper.getContactLastUpdateTime().stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = 0L
        )

    init {
        getContacts()
    }

    fun getContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            contactHelper.fetchContacts(lastUpdatedTime.value).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _contactList.value = result.data
                        if (result.data.isNotEmpty()) {
                            dataStoreHelper.setContactLastUpdateTime(System.currentTimeMillis())
                        }
                    }
                    is Result.Loading -> Unit
                    is Result.Error -> Unit
                }
            }
        }
    }
}