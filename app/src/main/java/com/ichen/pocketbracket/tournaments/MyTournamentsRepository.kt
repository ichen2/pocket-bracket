package com.ichen.pocketbracket.tournaments

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.ichen.pocketbracket.GetUserTournamentsQuery
import com.ichen.pocketbracket.home.apiKey
import com.ichen.pocketbracket.utils.API_ENDPOINT
import com.ichen.pocketbracket.utils.AuthorizationInterceptor
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.OkHttpClient

enum class MyTournamentsJobs {
    GET_EVENTS,
}

class MyTournamentsRepository {

    var currentJob: Job? = null

    suspend fun getUserEvents(
        page: Int,
        perPage: Int,
        context: Context,
        onResponse: (com.apollographql.apollo.api.Response<GetUserTournamentsQuery.Data>?) -> Unit
    ) {
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
                            GetUserTournamentsQuery(page, perPage)
                        ).await()
                    }
                } catch (e: ApolloException) {
                    // handle protocol errors
                    null
                }
                onResponse(response)
            }
        }
    }
}