package com.ichen.pocketbracket.tournaments

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ichen.pocketbracket.utils.Field
import com.ichen.pocketbracket.utils.Status
import kotlinx.coroutines.launch

class MyTournamentsViewModel : ViewModel() {
    private val repository = MyTournamentsRepository()
    var userId: MutableState<Field<String?>> = mutableStateOf(Field(null, Status.NOT_STARTED))

    fun getCurrentUser(context: Context) {
        userId.value = Field(null, Status.LOADING)
        viewModelScope.launch {
            repository.getCurrentUser(context) { response ->
                val id = response?.data?.currentUser?.id
                userId .value= Field(id, if(id == null) Status.ERROR else Status.SUCCESS)
            }
        }
    }

    fun cleanup() {
        repository.jobs.forEach { pair ->
            pair.value?.cancel()
        }
    }
}