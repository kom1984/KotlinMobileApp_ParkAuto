package com.example.mykotlinmobileapp.view_model


import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mykotlinmobileapp.repository.AuthRepository
import java.security.InvalidParameterException


class RegisterActivityViewModelFactory(private  val authRepository: AuthRepository, val application: Application) :ViewModelProvider.Factory{
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(RegisterActivityViewModel::class.java)){
            return RegisterActivityViewModel(authRepository,application) as T

        }
        throw  InvalidParameterException("Unable to construct RegisterActivityViewModel")
    }
}