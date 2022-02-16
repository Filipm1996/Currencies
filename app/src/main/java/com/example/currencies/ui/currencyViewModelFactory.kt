package com.example.currencies.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.currencies.data.repositories.repository

class currencyViewModelFactory(
private val repository: repository
) : ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return currencyViewModel(repository) as T
    }
}