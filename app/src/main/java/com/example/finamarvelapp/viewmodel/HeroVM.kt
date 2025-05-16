package com.example.finamarvelapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.*
import com.example.finamarvelapp.data.model.Hero
import com.example.finamarvelapp.data.remote.RetrofitInstance
import kotlinx.coroutines.launch

class HeroViewModel : ViewModel() {

    var heroList by mutableStateOf<List<Hero>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set

    init {
        refreshHeroes()
    }

    private fun refreshHeroes() {
        viewModelScope.launch {
            isLoading = true
            heroList = try {
                RetrofitInstance.api.fetchHeroesList()
            } catch (_: Exception) {
                emptyList()
            } finally {
                isLoading = false
            }
        }
    }

    fun reloadHeroes() {
        refreshHeroes()
    }
}
