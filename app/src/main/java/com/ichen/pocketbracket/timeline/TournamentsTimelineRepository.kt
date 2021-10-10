package com.ichen.pocketbracket.timeline

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.ichen.pocketbracket.BuildConfig
import com.ichen.pocketbracket.GetTournamentsQuery
import com.ichen.pocketbracket.models.Tournament
import com.ichen.pocketbracket.models.TournamentFilter
import com.ichen.pocketbracket.utils.Field
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.OkHttpClient

const val API_ENDPOINT = "https://api.smash.gg/gql/alpha"

class TournamentsTimelineRepository {
    suspend fun getTournaments(
        filter: TournamentFilter,
        context: Context,
        onResponse: (com.apollographql.apollo.api.Response<GetTournamentsQuery.Data>?) -> Unit
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
            launch {
                val response = try {
                    apolloClient.query(GetTournamentsQuery(perPage = 4, Input.optional("")))
                        .toDeferred().await()
                } catch (e: ApolloException) {
                    // handle protocol errors
                    null
                }
                onResponse(response)
            }
        }
    }
}

private class AuthorizationInterceptor(val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${BuildConfig.SMASHGG_API_KEY}")
            .build()

        return chain.proceed(request)
    }
}