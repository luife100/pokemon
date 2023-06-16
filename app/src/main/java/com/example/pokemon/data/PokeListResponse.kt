package com.example.pokemon.data

data class PokeListResponse(
    val count:String?,
    val next: String?,
    val previous:String?,
    val results: List<PokemonListItem>
)
