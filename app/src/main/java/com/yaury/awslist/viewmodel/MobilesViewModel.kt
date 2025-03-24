package com.yaury.awslist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yaury.awslist.data.MobilesRepository
import com.yaury.awslist.data.Result
import com.yaury.awslist.model.MobileItem
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MobilesViewModel(
    private val mobilesRepository: MobilesRepository
): ViewModel() {

    private val _mobiles = MutableStateFlow<List<MobileItem>>(emptyList())
    val mobiles = _mobiles.asStateFlow()

    private val _showErrorToastChannel = Channel<Boolean>()
    val showErrorToastChannel = _showErrorToastChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            mobilesRepository.getMobilesList().collectLatest { result ->
                when(result) {
                    is Result.Success -> {
                        result.data?.let { mobiles ->
                            _mobiles.update { mobiles }
                        }
                    }
                    is Result.Error -> {
                        _showErrorToastChannel.send(true)
                    }
                }
            }
        }
    }
}






