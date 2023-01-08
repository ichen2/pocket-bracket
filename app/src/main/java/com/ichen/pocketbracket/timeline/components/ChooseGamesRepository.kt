package com.ichen.pocketbracket.timeline.components

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional

import com.apollographql.apollo3.exception.ApolloException
import com.apollographql.apollo3.network.okHttpClient
import com.ichen.pocketbracket.GetVideogamesQuery
import com.ichen.pocketbracket.home.apiKey
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
        onResponse: (ApolloResponse<GetVideogamesQuery.Data>?) -> Unit
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
                                page = filter.page,
                                perPage = filter.perPage,
                                name = Optional.presentIfNotNull(filter.name)
                            )
                        ).execute()
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