package com.example.pokemon.data

import com.example.pokemon.network.PokeAPI
import com.example.pokemon.network.PokemonService

class HomeRepository {
   companion object{
       val limit = 20
   }
    suspend fun getPokemonsList(offset:Int?): List<PokemonListItem> {
        val pokemonService = PokeAPI.getApiClient().create(PokemonService::class.java)
        return pokemonService.getPokemonsList(offset).results
    }

    suspend fun getPokemon(id:Int): Pokemon {
        val pokemonService = PokeAPI.getApiClient().create(PokemonService::class.java)
        return pokemonService.getPokemon(id)
    }
}