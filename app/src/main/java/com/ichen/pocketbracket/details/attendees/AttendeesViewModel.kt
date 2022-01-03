package com.ichen.pocketbracket.details.attendees

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.api.Response
import com.ichen.pocketbracket.GetParticipantsQuery
import com.ichen.pocketbracket.details.tournament
import com.ichen.pocketbracket.models.Attendee
import com.ichen.pocketbracket.utils.Field
import com.ichen.pocketbracket.utils.Status
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AttendeesViewModel : ViewModel() {
    private val repository = AttendeesRepository()
    private var currentJob: Job? = null
    val attendees: MutableState<Field<List<Attendee>>> = mutableStateOf(
        Field(
            listOf(),
            Status.NOT_STARTED
        )
    )

    fun getAttendees(context: Context) {
        attendees.value = Field(listOf(), Status.LOADING)
        currentJob = viewModelScope.launch {
            repository.getAttendees(tournament!!.id.toString(), context) { response ->
                val parsedResponse = parseGetParticipantsResponse(response)
                attendees.value = Field(
                    parsedResponse ?: listOf(),
                    if (parsedResponse == null) Status.ERROR else Status.SUCCESS
                )
            }
        }
    }

    fun parseGetParticipantsResponse(response: Response<GetParticipantsQuery.Data>?): List<Attendee>? {
        val nodes = response?.data?.tournament?.participants?.nodes ?: return null
        return nodes.filter { node ->
            node?.id != null
            node?.gamerTag != null
        }.map { node ->
            Attendee(
                id = node!!.id!!,
                tag = node.gamerTag!!
            )
        }
    }

    fun cleanup() {
        currentJob?.cancel()
    }
}