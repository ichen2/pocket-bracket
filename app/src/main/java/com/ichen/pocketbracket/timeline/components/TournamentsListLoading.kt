package com.ichen.pocketbracket.timeline.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ichen.pocketbracket.components.ShimmerAnimation

@Composable
fun TournamentsListLoading(numItems: Int = 2) = Column(
    Modifier.verticalScroll(
        rememberScrollState()
    )
) {
    for (i in 0..Integer.max(numItems, 2)) {
        ShimmerAnimation { brush ->
            TournamentCardLoading(brush = brush)
        }
    }
}