package com.tfgmanuel.dungeonvault.presentacion.viewmodel.loginviewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginViewModel: ViewModel() {
    private val _email = MutableLiveData<String>()
    private val _password = MutableLiveData<String>()
    private val _loginError = MutableLiveData<String>()

    val email: LiveData<String> = _email
    val password: LiveData<String> = _password
    val loginError: LiveData<String> = _loginError

    fun onLoginChanged(email: String, password: String) {
        _email.value = email
        _password.value = password
    }


}