package com.ichen.pocketbracket.tournaments

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.ichen.pocketbracket.GetUserEventsQuery
import com.ichen.pocketbracket.timeline.API_ENDPOINT
import com.ichen.pocketbracket.utils.AuthorizationInterceptor
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

enum class MyTournamentsJobs {
    GET_EVENTS,
}

class MyTournamentsRepository {

    val jobs : MutableMap<MyTournamentsJobs, Job?> = mutableMapOf(MyTournamentsJobs.GET_EVENTS to null)

    suspend fun getUserEvents(
        page: Int,
        perPage: Int,
        context: Context,
        onResponse: (com.apollographql.apollo.api.Response<GetUserEventsQuery.Data>?) -> Unit
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
            jobs[MyTournamentsJobs.GET_EVENTS] = launch {
                val response = try {
                    apolloClient.query(
                        GetUserEventsQuery(page, perPage)
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