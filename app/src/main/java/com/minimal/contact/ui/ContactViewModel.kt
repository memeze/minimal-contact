package com.minimal.contact.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.minimal.contact.data.model.Contact
import com.minimal.contact.util.ContactHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactViewModel @Inject constructor(
    private val contactHelper: ContactHelper
) : ViewModel() {

    private val _contactState = MutableStateFlow<ContactState>(ContactState.Pending)
    val contactState = _contactState

    init {
        fetchContacts()
    }

    private fun fetchContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            _contactState.emit(ContactState.Loading)
            contactHelper.fetchContacts().collect { result ->
                _contactState.emit(ContactState.Loaded(result))
            }
        }
    }
}

sealed class ContactState {
    object Pending : ContactState()
    object Loading : ContactState()
    data class Loaded(val contacts: List<Contact>) : ContactState()
}