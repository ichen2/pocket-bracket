package com.ichen.pocketbracket.timeline

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ichen.pocketbracket.models.Tournament
import com.ichen.pocketbracket.models.TournamentFilter
import com.ichen.pocketbracket.utils.Field
import com.ichen.pocketbracket.utils.Status
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class TournamentsTimelineViewModel : ViewModel() {

    private val repository = TournamentsTimelineRepository()
    val tournaments: Field<List<Tournament>> by mutableStateOf(
        Field(
            listOf(),
            Status.NOT_STARTED
        )
    )

    fun getTournaments(filter: TournamentFilter = TournamentFilter(), context: Context) {
        viewModelScope.launch { repository.getTournaments(filter, tournaments, context = context) }
    }
}