package com.example.finamarvelapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.finamarvelapp.data.model.Hero
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesVM : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()
    private val favsRef = firestore.collection("favorites")

    private val _favHeroes = MutableStateFlow<List<Hero>>(emptyList())
    val favorites: StateFlow<List<Hero>> = _favHeroes.asStateFlow()

    init {
        subscribeToFavorites()
    }

    fun addFavorite(hero: Hero) {
        viewModelScope.launch {
            favsRef.document(hero.id.toString()).set(hero)
        }
    }

    fun removeFavorite(hero: Hero) {
        viewModelScope.launch {
            favsRef.document(hero.id.toString()).delete()
        }
    }

    private fun subscribeToFavorites() {
        favsRef.addSnapshotListener { snapshot, error ->
            if (error == null && snapshot != null) {
                _favHeroes.value = snapshot.toObjects(Hero::class.java)
            }
        }
    }
}
