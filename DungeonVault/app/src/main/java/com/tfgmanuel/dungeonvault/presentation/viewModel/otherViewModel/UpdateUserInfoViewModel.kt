package com.tfgmanuel.dungeonvault.presentation.viewModel.otherViewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tfgmanuel.dungeonvault.data.UserDataStore
import com.tfgmanuel.dungeonvault.data.repository.AuthRepository
import com.tfgmanuel.dungeonvault.navigation.NavManager
import com.tfgmanuel.dungeonvault.presentation.states.UpdateUserState
import com.tfgmanuel.dungeonvault.presentation.viewModel.ContextProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpdateUserInfoViewModel @Inject constructor(
    private val userDataStore: UserDataStore,
    private val authRepository: AuthRepository,
    val navManager: NavManager,
    private val contextProvider: ContextProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow(UpdateUserState())
    val uiState: StateFlow<UpdateUserState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val result = authRepository.getUser()

            if (result.isSuccess) {
                _uiState.value = _uiState.value.copy(
                    originalNickname = userDataStore.getNickname().first()!!,
                    nickname = userDataStore.getNickname().first()!!,
                    originalAvatar = userDataStore.getAvatar().first()!!
                )
            }
        }
    }

    fun onUpdateChanged(nickname: String, avatarUri: Uri) {
        _uiState.value = _uiState.value.copy(
            nickname = nickname,
            avatarUri = avatarUri
        )
    }

    fun onSaveClick() {
        viewModelScope.launch {
            val result = authRepository.update(
                nickname = _uiState.value.nickname,
                avatarUri = _uiState.value.avatarUri,
                context = contextProvider.getContext()
            )

            if (result.isSuccess) {
                goBack()
            }
        }
    }

    fun goBack() {
        viewModelScope.launch {
            navManager.goBack()
        }
    }
}