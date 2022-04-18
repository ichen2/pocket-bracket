package com.ichen.pocketbracket.models

enum class VideogameName {
    SMASH,
    SMASH_MELEE,
    SMASH_PM,
    SMASH_P_PLUS,
    SMASH_ULTIMATE,
    SMASH_BRAWL,
    SFV_ARCADE,
    ROCKET_LEAGUE,
    KILLER_INSTINCT,
    BRAWLHALLA,
    GG_STRIVE,
    VALORANT,
    CS_GO,
    LEAGUE_OF_LEGENDS,
    TEKKEN_7,
}

data class Videogame(val id: Int, val displayName: String)

val videogamesList = listOf(
    Videogame(1, "Melee"),
    Videogame(1386, "Ultimate"),
    Videogame(7, "Street Fighter V: Arcade Edition"),
    Videogame(14, "Rocket League"),
    Videogame(4, "Super Smash Bros"),
    Videogame(19, "Killer Instinct"),
    Videogame(15, "Brawlhalla"),
    Videogame(33945, "Guilty Gear Strive"),
    Videogame(34223, "Valorant"),
    Videogame(12, "CS:GO"),
    Videogame(10, "League of Legends"),
    Videogame(33602, "Project+"),
    Videogame(17, "Tekken 7")
)
val videogamesMap : Map<Int, Videogame> = videogamesList.associateBy { videogame -> (videogame.id)}