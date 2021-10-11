package com.ichen.pocketbracket.timeline

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ichen.pocketbracket.models.*
import com.ichen.pocketbracket.utils.Field
import com.ichen.pocketbracket.utils.Status
import com.ichen.pocketbracket.utils.convertBigDecimalToDate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*

class TournamentsTimelineViewModel : ViewModel() {

    private val repository = TournamentsTimelineRepository()
    val tournaments : MutableState<Field<List<Tournament>>> = mutableStateOf(
        Field(
            listOf(),
            Status.NOT_STARTED
        )
    )

    fun getTournaments(filter: TournamentFilter = TournamentFilter(), context: Context) {
        tournaments.value = tournaments.value.withStatus(Status.LOADING)
        viewModelScope.launch {
            repository.getTournaments(filter = filter, context = context) { response ->
                if(response?.data?.tournaments?.nodes == null || response.data?.tournaments?.nodes?.size == 0) {
                    tournaments.value = Field(listOf(), Status.ERROR)
                } else {
                    val nodes = response.data?.tournaments?.nodes
                    if(nodes != null) {
                        // SUS
                        val unfilteredTournaments =
                            nodes.filter { node ->
                                node?.id != null
                                node?.name != null
                            }.map { node ->
                                Tournament(
                                    id = node!!.id!!.toInt(),
                                    name = node.name!!,
                                    startAt = convertBigDecimalToDate(node.startAt),
                                    endAt = convertBigDecimalToDate(node.endAt),
                                    isOnline = node.isOnline,
                                    isRegistrationOpen = node.isRegistrationOpen,
                                    numAttendees = node.numAttendees,
                                    state = when(node.state) {
                                        0 -> ActivityState.CREATED
                                        1 -> ActivityState.ACTIVE
                                        2 -> ActivityState.COMPLETED
                                        else -> null
                                    },
                                    imageUrl = node.images?.getOrNull(0)?.url,
                                    events = node.events?.filter { event ->
                                        event?.name != null
                                    }?.map { event ->
                                        Event(
                                            id = 0,
                                            name = event!!.name!!,
                                            numEntrants = event.numEntrants,
                                            startAt = convertBigDecimalToDate(node.startAt),
                                            videogame = if(event.videogame?.id != null && videogamesMap.containsKey(event.videogame.id.toInt())) videogamesMap[event.videogame.id.toInt()] else null
                                        )
                                    }
                                )
                            }
                        tournaments.value = Field(unfilteredTournaments, Status.SUCCESS)
                    }
                }
            }
        }
    }
}