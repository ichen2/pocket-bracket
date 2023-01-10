package com.ichen.pocketbracket.timeline

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.apollographql.apollo3.exception.ApolloException
import com.apollographql.apollo3.network.okHttpClient
import com.ichen.pocketbracket.GetTournamentsQuery
import com.ichen.pocketbracket.home.apiKey
import com.ichen.pocketbracket.models.TournamentFilter
import com.ichen.pocketbracket.models.TournamentRegistrationStatus
import com.ichen.pocketbracket.models.TournamentType
import com.ichen.pocketbracket.type.TournamentLocationFilter
import com.ichen.pocketbracket.utils.API_ENDPOINT
import com.ichen.pocketbracket.utils.AuthorizationInterceptor
import com.ichen.pocketbracket.utils.addDays
import com.ichen.pocketbracket.utils.addHours
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.OkHttpClient
import java.util.*

class TournamentsTimelineRepository {

    suspend fun getTournaments(
        filter: TournamentFilter,
        context: Context,
    ): ApolloResponse<GetTournamentsQuery.Data>? {
         val apolloClient = ApolloClient.Builder()
            .serverUrl(API_ENDPOINT)
            .okHttpClient(
                OkHttpClient.Builder()
                    .addInterceptor(AuthorizationInterceptor(context, apiKey!!))
                    .build()
            )
            .build()
        return try {
            withTimeoutOrNull(15000) {
                apolloClient.query(
                    GetTournamentsQuery(
                        page = filter.page,
                        perPage = filter.perPage,
                        name = Optional.present(filter.name),
                        isOnline = when (filter.type) {
                            TournamentType.IS_ONLINE -> Optional.present(true)
                            TournamentType.IS_OFFLINE -> Optional.present(false)
                            TournamentType.NO_FILTER -> Optional.Absent
                        },
                        location = if (filter.location != null) Optional.presentIfNotNull(
                            TournamentLocationFilter(
                                distanceFrom = Optional.presentIfNotNull(filter.location?.let { "${it.center.latitude},${it.center.longitude}" }),
                                distance = Optional.presentIfNotNull(filter.location?.let { "${it.radius.toInt()}mi" })
                            )
                        ) else Optional.Absent,
                        registrationIsOpen = when (filter.registration) {
                            TournamentRegistrationStatus.OPEN -> Optional.present(true)
                            TournamentRegistrationStatus.CLOSED -> Optional.present(false)
                            TournamentRegistrationStatus.NO_FILTER -> Optional.Absent
                        },
                        afterDate = Optional.presentIfNotNull(
                            filter.dates?.start?.time?.let { it / 1000 }
                        ),
                        beforeDate = Optional.presentIfNotNull(
                            /*
                            cap the max date at 1 year in the future,
                            unless a further date is explicitly provided by the user
                             */
                            filter.dates?.end?.addHours(1)?.time?.let { it / 1000 }
                                ?: (Date().addDays(365).time / 1000)
                        ),
                        videogameIds = Optional.presentIfNotNull(
                            filter.games?.map { it -> it.id.toString() }
                        )
                    )
                ).execute()
            }
        } catch (e: ApolloException) {
            // handle protocol errors
            null
        }
    }
}