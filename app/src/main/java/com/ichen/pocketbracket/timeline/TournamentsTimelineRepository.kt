package com.ichen.pocketbracket.timeline

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional

import com.apollographql.apollo3.exception.ApolloException
import com.apollographql.apollo3.network.okHttpClient
import com.ichen.pocketbracket.GetTournamentsQuery
import com.ichen.pocketbracket.home.apiKey
import com.ichen.pocketbracket.models.*
import com.ichen.pocketbracket.type.TournamentLocationFilter
import com.ichen.pocketbracket.utils.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.OkHttpClient
import java.util.*

enum class TournamentsTimelineJobs {
    GET_TOURNAMENTS
}

class TournamentsTimelineRepository {

    var currentJob: Job? = null

    suspend fun getTournaments(
        filter: TournamentFilter,
        context: Context,
        onResponse: (ApolloResponse<GetTournamentsQuery.Data>?) -> Unit
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
                                name = Optional.presentIfNotNull(filter.name),
                                isOnline = when (filter.type) {
                                    TournamentType.IS_ONLINE -> Optional.presentIfNotNull(true)
                                    TournamentType.IS_OFFLINE -> Optional.presentIfNotNull(false)
                                    else -> Optional.Absent
                                },
                                location = if (filter.location != null) Optional.presentIfNotNull(
                                    TournamentLocationFilter(
                                        distanceFrom = Optional.presentIfNotNull("${filter.location.center.latitude},${filter.location.center.longitude}"),
                                        distance = Optional.presentIfNotNull("${filter.location.radius.toInt()}mi")
                                    )
                                ) else Optional.Absent,
                                registrationIsOpen = when (filter.registration) {
                                    TournamentRegistrationStatus.OPEN -> Optional.presentIfNotNull(true)
                                    TournamentRegistrationStatus.CLOSED -> Optional.presentIfNotNull(false)
                                    else -> Optional.Absent
                                },
                                afterDate = if (filter.dates != null) Optional.presentIfNotNull(filter.dates.start.time / 1000) else Optional.Absent,
                                beforeDate = if (filter.dates != null) Optional.presentIfNotNull(
                                    filter.dates.end.addHours(
                                        1
                                    ).time / 1000
                                ) else Optional.presentIfNotNull(
                                    Date().addDays(365).time / 1000
                                ),
                                videogameIds = if (filter.games != null) Optional.presentIfNotNull(filter.games.map { videogame ->
                                    videogame.id.toString()
                                }) else Optional.Absent
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