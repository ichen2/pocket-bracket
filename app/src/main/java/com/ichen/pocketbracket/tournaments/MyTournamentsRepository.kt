package com.ichen.pocketbracket.tournaments

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse

import com.apollographql.apollo3.exception.ApolloException
import com.apollographql.apollo3.network.okHttpClient
import com.ichen.pocketbracket.GetUserTournamentsQuery
import com.ichen.pocketbracket.home.apiKey
import com.ichen.pocketbracket.utils.API_ENDPOINT
import com.ichen.pocketbracket.utils.AuthorizationInterceptor
import kotlinx.coroutines.*
import okhttp3.OkHttpClient

enum class MyTournamentsJobs {
    GET_EVENTS,
}

class MyTournamentsRepository {

    suspend fun getUserEvents(
        page: Int,
        perPage: Int,
        context: Context,
    ): ApolloResponse<GetUserTournamentsQuery.Data>? {
        val apolloClient = ApolloClient.Builder()
            .serverUrl(API_ENDPOINT)
            .okHttpClient(
                OkHttpClient.Builder()
                    .addInterceptor(AuthorizationInterceptor(context, apiKey!!))
                    .build()
            )
            .build()

        // handle protocol errors
        return try {
            withTimeoutOrNull(15000) {
                apolloClient.query(
                    query = GetUserTournamentsQuery(page, perPage)
                ).execute()
            }
        } catch (e: ApolloException) {
            // handle protocol errors
            null
        }
    }
}