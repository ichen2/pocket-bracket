package com.ichen.pocketbracket.tournaments

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.ichen.pocketbracket.timeline.TournamentsTimelineViewModel
import com.ichen.pocketbracket.utils.Status

@Composable
fun ColumnScope.MyTournamentsScreen(
    viewModel: MyTournamentsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) = Column(Modifier.weight(1f)) {
    Text("User Id: ${viewModel.userId.value.data ?: "null"}", color = MaterialTheme.colors.primary)
    Text("Status: ${viewModel.userId.value.status}", color = MaterialTheme.colors.primary)
    if(viewModel.userId.value.status == Status.NOT_STARTED) {
        viewModel.getCurrentUser(LocalContext.current)
    }
}