package com.bemily.samplecontact.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bemily.samplecontact.data.model.Contact
import com.bemily.samplecontact.data.model.Result
import com.bemily.samplecontact.util.ContactHelper
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

    private val _contactState = MutableStateFlow<Result<List<Contact>>>(Result.Success(emptyList()))
    var contactState = _contactState

    init {
        getContacts()
    }

    private fun getContacts() {
        viewModelScope.launch(Dispatchers.IO) {
            contactHelper.fetchContacts().collect { result ->
                _contactState.value = result
            }
        }
    }
}