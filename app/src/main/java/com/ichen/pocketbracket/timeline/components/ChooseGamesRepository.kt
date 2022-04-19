package com.ichen.pocketbracket.timeline.components

import android.content.Context
import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.ichen.pocketbracket.GetVideogamesQuery
import com.ichen.pocketbracket.apiKey
import com.ichen.pocketbracket.models.VideogameFilter
import com.ichen.pocketbracket.utils.API_ENDPOINT
import com.ichen.pocketbracket.utils.AuthorizationInterceptor
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.OkHttpClient

class ChooseGamesRepository {

    var currentJob: Job? = null

    suspend fun getVideogames(
        filter: VideogameFilter,
        context: Context,
        onResponse: (com.apollographql.apollo.api.Response<GetVideogamesQuery.Data>?) -> Unit
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
                            GetVideogamesQuery(
                                page = 0,
                                perPage = 10,
                                name = Input.optional(filter.name)
                            )
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