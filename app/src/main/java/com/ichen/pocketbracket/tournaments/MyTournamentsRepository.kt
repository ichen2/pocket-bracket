package com.ichen.pocketbracket.tournaments

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.ichen.pocketbracket.GetCurrentUserQuery
import com.ichen.pocketbracket.GetTournamentsQuery
import com.ichen.pocketbracket.models.TournamentFilter
import com.ichen.pocketbracket.timeline.API_ENDPOINT
import com.ichen.pocketbracket.utils.AuthorizationInterceptor
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

enum class MyTournamentsJobs {
    GET_CURRENT_USER
}

class MyTournamentsRepository {

    val jobs : MutableMap<MyTournamentsJobs, Job?> = mutableMapOf(MyTournamentsJobs.GET_CURRENT_USER to null)

    suspend fun getCurrentUser(
        context: Context,
        onResponse: (com.apollographql.apollo.api.Response<GetCurrentUserQuery.Data>?) -> Unit
    ) {
        val apolloClient = ApolloClient.builder()
            .serverUrl(API_ENDPOINT)
            .okHttpClient(
                OkHttpClient.Builder()
                    .addInterceptor(AuthorizationInterceptor(context))
                    .build()
            )
            .build()
        coroutineScope {
            jobs[MyTournamentsJobs.GET_CURRENT_USER] = launch {
                val response = try {
                    apolloClient.query(
                        GetCurrentUserQuery()
                    ).toDeferred().await()
                } catch (e: ApolloException) {
                    // handle protocol errors
                    null
                }
                onResponse(response)
            }
        }
    }
}