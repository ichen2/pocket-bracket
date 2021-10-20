package com.ichen.pocketbracket.auth

import android.content.Context
import android.content.Intent
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.ichen.pocketbracket.GetUserDetailsQuery
import com.ichen.pocketbracket.MainActivity
import com.ichen.pocketbracket.apiKey
import com.ichen.pocketbracket.utils.*
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.OkHttpClient

class AuthViewModel : ViewModel() {
    val apiKeyText = mutableStateOf(
        Field("", Status.NOT_STARTED)
    )

    fun verifyApiKey(context: Context) {
        if(apiKeyText.value.status != Status.LOADING) {
            apiKeyText.value = apiKeyText.value.withStatus(Status.LOADING)
            val apolloClient = ApolloClient.builder()
                .serverUrl(API_ENDPOINT)
                .okHttpClient(
                    OkHttpClient.Builder()
                        .addInterceptor(AuthorizationInterceptor(context, apiKeyText.value.data))
                        .build()
                )
                .build()
            viewModelScope.launch {
                val response = try {
                    withTimeoutOrNull(10000) {
                        apolloClient.query(
                            GetUserDetailsQuery()
                        ).await()
                    }
                } catch (e: ApolloException) {
                    null
                }
                if (response != null) {
                    context.startActivity(Intent(context, MainActivity::class.java).apply {
                        putExtra(API_KEY_STORAGE_KEY, apiKeyText.value.data)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    })
                } else {
                    apiKeyText.value = apiKeyText.value.withStatus(Status.ERROR)
                }
            }
        }
    }
}