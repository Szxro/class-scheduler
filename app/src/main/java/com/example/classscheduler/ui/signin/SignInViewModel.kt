package com.example.classscheduler.ui.signin

import com.example.classscheduler.common.BaseViewModel
import com.example.classscheduler.domain.interfaces.AuthRepository
import com.example.classscheduler.util.ext.isPasswordValid
import com.example.classscheduler.util.ext.isValidEmail
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {
    fun signIn(
        email: String,
        password: String
    ):Unit{
        if(!email.isValidEmail()){
            // TODO: CREATE A DIALOG TO SHOW THE USER THAT THE EMAIL PROVIDE IS INVALID
            return;
        }

        if(!password.isPasswordValid()){
            // TODO: CREATE A DIALOG TO SHOW THE USER THAT THE PASSWORD PROVIDE IS INVALID
            return;
        }

        launchCatching{
            authRepository.signIn(email,password);
        }
    }
}