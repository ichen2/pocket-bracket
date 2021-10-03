package com.ichen.pocketbracket.models

enum class VideogameName {
    SMASH_MELEE,
    SMASH_PM,
    SMASH_ULTIMATE,
    SMASH_BRAWL,
    SFV_ARCADE,
    ROCKET_LEAGUE,
    KILLER_INSTINCT,
}

data class Videogame(val name: VideogameName, val id: Int, val displayName: String)

val videogamesList = listOf(
    Videogame(VideogameName.SMASH_MELEE, 1, "Melee")
)
val videogamesMap = videogamesList.associateBy { videogame -> (videogame.id)}