package com.example.pokemon.viewModel

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pokemon.data.Pokemon
import com.example.pokemon.data.PokemonListItem
import com.example.pokemon.data.HomeRepository
import kotlinx.coroutines.launch

class HomeViewModel:ViewModel() {
    private var homeRepository: HomeRepository?=null
    private val _postModelListLiveData = MutableLiveData<SnapshotStateList<PokemonListItem>>()
    val postModelListLiveData :LiveData<SnapshotStateList<PokemonListItem>> = _postModelListLiveData
    private val _pokemonLiveData = MutableLiveData<Pokemon>()
     val pokemonLiveData:LiveData<Pokemon> = _pokemonLiveData
     var offset:Int? = null
    init {
        homeRepository = HomeRepository()
        _postModelListLiveData.value = SnapshotStateList()
       getPokemonsList(offset)
    }

    fun getPokemonsList(offset:Int?){
           viewModelScope.launch {
               homeRepository?.getPokemonsList(offset)
                   ?.let { _postModelListLiveData.value?.addAll(it) }
           }}

        fun getPokemon(id:Int){
            viewModelScope.launch {
                _pokemonLiveData.value=homeRepository?.getPokemon(id)
            }

    }

    fun increaseOffset():Int?{
        offset = offset?.plus(HomeRepository.limit) ?: 20
        offset?.let { if(it>=1281) offset=null }
        getPokemonsList(offset)
        return offset
    }
    fun decreaseOffset():Int?{
        offset = offset?.minus(HomeRepository.limit)
        offset?.let { if(it<=0) offset=null }
        getPokemonsList(offset)
        return offset
    }

    fun increaseList(): Int? {
        offset = offset?.plus(HomeRepository.limit) ?: 20
        getPokemonsList(offset)
        return offset
    }
}