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
}

data class Videogame(val name: VideogameName, val id: Int, val displayName: String)

val videogamesList = listOf(
    Videogame(VideogameName.SMASH_MELEE, 1, "Melee"),
    Videogame(VideogameName.SMASH_ULTIMATE, 1386, "Ultimate"),
    Videogame(VideogameName.SFV_ARCADE, 7, "Street Fighter V: Arcade Edition"),
    Videogame(VideogameName.ROCKET_LEAGUE, 14, "Rocket League"),
    Videogame(VideogameName.SMASH, 4, "Super Smash Bros"),
    Videogame(VideogameName.KILLER_INSTINCT, 19, "Killer Instinct"),
    Videogame(VideogameName.BRAWLHALLA, 15, "Brawlhalla"),
    Videogame(VideogameName.GG_STRIVE, 33945, "Guilty Gear Strive"),
    Videogame(VideogameName.VALORANT, 34223, "Valorant"),
    Videogame(VideogameName.CS_GO, 12, "CS:GO"),
    Videogame(VideogameName.LEAGUE_OF_LEGENDS, 10, "League of Legends"),
    Videogame(VideogameName.SMASH_P_PLUS, 33602, "Project+"),
)
val videogamesMap : Map<Int, Videogame> = videogamesList.associateBy { videogame -> (videogame.id)}