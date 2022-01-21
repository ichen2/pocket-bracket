package com.ichen.pocketbracket.timeline

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.coroutines.toDeferred
import com.apollographql.apollo.exception.ApolloException
import com.ichen.pocketbracket.BuildConfig
import com.ichen.pocketbracket.GetTournamentsQuery
import com.ichen.pocketbracket.apiKey
import com.ichen.pocketbracket.models.Tournament
import com.ichen.pocketbracket.models.TournamentFilter
import com.ichen.pocketbracket.models.TournamentRegistrationStatus
import com.ichen.pocketbracket.models.TournamentType
import com.ichen.pocketbracket.tournaments.MyTournamentsJobs
import com.ichen.pocketbracket.type.TournamentLocationFilter
import com.ichen.pocketbracket.utils.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import java.time.LocalDateTime
import java.util.*

enum class TournamentsTimelineJobs {
    GET_TOURNAMENTS
}

class TournamentsTimelineRepository {

    var currentJob: Job? = null

    suspend fun getTournaments(
        filter: TournamentFilter,
        context: Context,
        onResponse: (com.apollographql.apollo.api.Response<GetTournamentsQuery.Data>?) -> Unit
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
                            GetTournamentsQuery(
                                page = filter.page,
                                perPage = filter.perPage,
                                name = Input.optional(filter.name),
                                isOnline = when (filter.type) {
                                    TournamentType.IS_ONLINE -> Input.optional(true)
                                    TournamentType.IS_OFFLINE -> Input.optional(false)
                                    else -> Input.absent()
                                },
                                location = if (filter.location != null) Input.optional(
                                    TournamentLocationFilter(
                                        distanceFrom = Input.optional("${filter.location.center.latitude},${filter.location.center.longitude}"),
                                        distance = Input.optional("${filter.location.radius.toInt()}mi")
                                    )
                                ) else Input.absent(),
                                registrationIsOpen = when (filter.registration) {
                                    TournamentRegistrationStatus.OPEN -> Input.optional(true)
                                    TournamentRegistrationStatus.CLOSED -> Input.optional(false)
                                    else -> Input.absent()
                                },
                                afterDate = if (filter.dates != null) Input.optional(filter.dates.start.time / 1000) else Input.absent(),
                                beforeDate = if (filter.dates != null) Input.optional(filter.dates.end.addHours(1).time / 1000) else Input.optional(
                                    Date().addDays(365).time / 1000
                                ),
                                videogameIds = if (filter.games != null) Input.optional(filter.games.map { videogame ->
                                    videogame.id.toString()
                                }) else Input.absent()
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