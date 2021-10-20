package com.ichen.pocketbracket.profile

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.ichen.pocketbracket.GetUserDetailsQuery
import com.ichen.pocketbracket.GetUserEventsQuery
import com.ichen.pocketbracket.apiKey
import com.ichen.pocketbracket.utils.API_ENDPOINT
import com.ichen.pocketbracket.utils.AuthorizationInterceptor
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.OkHttpClient

enum class MyProfileJobs {
    GET_USER_DETAILS
}

class MyProfileRepository {
    val jobs: MutableMap<MyProfileJobs, Job?> =
        mutableMapOf(MyProfileJobs.GET_USER_DETAILS to null)

    suspend fun getUserDetails(
        context: Context,
        onResponse: (com.apollographql.apollo.api.Response<GetUserDetailsQuery.Data>?) -> Unit
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
            jobs[MyProfileJobs.GET_USER_DETAILS] = launch {
                val response = try {
                    withTimeoutOrNull(15000) {
                        apolloClient.query(
                            GetUserDetailsQuery()
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