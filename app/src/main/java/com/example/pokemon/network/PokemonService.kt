package com.example.pokemon.network

import com.example.pokemon.data.PokeListResponse
import com.example.pokemon.data.Pokemon
import com.example.pokemon.data.HomeRepository
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokemonService {

    @GET("pokemon")
     suspend fun getPokemonsList( @Query("offset") offset: Int?, @Query("limit") limit:Int = HomeRepository.limit): PokeListResponse

    @GET("pokemon/{id}")
    suspend fun getPokemon(@Path("id") id: Int): Pokemon
}