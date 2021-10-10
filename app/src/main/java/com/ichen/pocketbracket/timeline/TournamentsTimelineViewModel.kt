package com.ichen.pocketbracket.timeline

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ichen.pocketbracket.models.ActivityState
import com.ichen.pocketbracket.models.Tournament
import com.ichen.pocketbracket.models.TournamentFilter
import com.ichen.pocketbracket.models.testTournament
import com.ichen.pocketbracket.utils.Field
import com.ichen.pocketbracket.utils.Status
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
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
        viewModelScope.launch { repository.getTournaments(filter = filter, context = context) { response ->
                if(response?.data?.tournaments?.nodes == null || response.data?.tournaments?.nodes?.size == 0) {
                    tournaments.value = Field(listOf(), Status.ERROR)
                } else {
                    val nodes = response.data?.tournaments?.nodes
                    if(nodes != null) {
                        val unfilteredTournaments =
                            nodes.filter { node ->
                                node?.id != null
                                node?.name != null
                            }.map { node ->
                                Tournament(
                                    id = node!!.id!!.toInt(),
                                    name = node.name!!,
                                    startAt = if(node.startAt != null && node.startAt is BigInteger) Date((node.startAt).toLong()) else null,
                                    endAt = if(node.endAt != null && node.endAt is BigInteger) Date((node.endAt).toLong()) else null,
                                    isOnline = node.isOnline,
                                    isRegistrationOpen = node.isRegistrationOpen,
                                    numAttendees = null,
                                    state = when(node.state) {
                                        0 -> ActivityState.CREATED
                                        1 -> ActivityState.ACTIVE
                                        2 -> ActivityState.COMPLETED
                                        else -> null
                                    },
                                    imageUrl = null,
                                    events = null
                                )
                            }
                        tournaments.value = Field(unfilteredTournaments, Status.SUCCESS)
                    }
                }
            }
        }
    }
}