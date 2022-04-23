package com.ichen.pocketbracket.details.attendees


import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.ichen.pocketbracket.GetParticipantsQuery
import com.ichen.pocketbracket.home.apiKey
import com.ichen.pocketbracket.utils.API_ENDPOINT
import com.ichen.pocketbracket.utils.AuthorizationInterceptor
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.OkHttpClient

class AttendeesRepository {
    var currentJob: Job? = null

    suspend fun getAttendees(tournamentId: String, page: Int, context: Context, onResponse: (com.apollographql.apollo.api.Response<GetParticipantsQuery.Data>?) -> Unit) {
        val apolloClient = ApolloClient.builder()
            .serverUrl(API_ENDPOINT)
            .okHttpClient(
                OkHttpClient.Builder()
                    .addInterceptor(AuthorizationInterceptor(context, apiKey!!))
                    .build()
            )
            .build()
        coroutineScope {
            currentJob = launch {
                val response = try {
                    withTimeoutOrNull(15000) {
                        apolloClient.query(
                            GetParticipantsQuery(id = tournamentId, perPage = 50, page = page)
                        ).await()
                    }
                } catch (e: ApolloException) {
                    null
                }
                onResponse(response)
            }
        }
    }
}