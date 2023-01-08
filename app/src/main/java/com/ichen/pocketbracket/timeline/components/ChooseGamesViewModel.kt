package com.ichen.pocketbracket.timeline.components

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.ApolloResponse
import com.ichen.pocketbracket.GetVideogamesQuery
import com.ichen.pocketbracket.models.Videogame
import com.ichen.pocketbracket.models.VideogameFilter
import com.ichen.pocketbracket.utils.Field
import com.ichen.pocketbracket.utils.Status
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

open class ChooseGamesViewModel : ViewModel() {

    private val repository = ChooseGamesRepository()
    private var videogameFilter = VideogameFilter(perPage = 250)
    private var currentJob: Job? = null
    open val videogames: MutableState<Field<List<Videogame>>> = mutableStateOf(
        Field(
            listOf(),
            Status.NOT_STARTED
        )
    )

    open fun getVideogames(videogameFilter: VideogameFilter, context: Context) {
        currentJob?.cancel()
        this.videogameFilter = videogameFilter
        videogames.value = Field(listOf(), Status.LOADING)
        currentJob = viewModelScope.launch {
            repository.getVideogames(filter = videogameFilter, context = context) { response ->
                val parsedResponse = parseGetVideogamesResponse(response)
                videogames.value = Field(
                    parsedResponse ?: listOf(),
                    if (parsedResponse == null) Status.ERROR else Status.SUCCESS
                )
            }
        }
    }

    private fun parseGetVideogamesResponse(response: ApolloResponse<GetVideogamesQuery.Data>?): List<Videogame>? {
        val nodes = response?.data?.videogames?.nodes
        if (nodes == null || nodes.isEmpty()) {
            return listOf()
        } else {
            return nodes.filter { node ->
                node?.id != null && node.name != null
            }.map { node ->
                Videogame(id = node!!.id!!.toInt(), displayName = node.name!!)
            }
        }
    }

    open fun cleanup() {
        // TODO: Seems like there should be a better way to do this but idk how :/
        currentJob?.cancel()
        videogameFilter = VideogameFilter(perPage = 250)
        videogames.value = Field(
            listOf(),
            Status.NOT_STARTED
        )
    }
}
