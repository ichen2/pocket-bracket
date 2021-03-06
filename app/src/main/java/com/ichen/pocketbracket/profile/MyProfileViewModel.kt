package com.ichen.pocketbracket.profile

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ichen.pocketbracket.models.UserDetails
import com.ichen.pocketbracket.utils.Field
import com.ichen.pocketbracket.utils.SITE_ENDPOINT
import com.ichen.pocketbracket.utils.Status
import kotlinx.coroutines.launch

class MyProfileViewModel : ViewModel() {

    private val repository = MyProfileRepository()
    val userDetails: MutableState<Field<UserDetails?>> =
        mutableStateOf(Field(null, Status.NOT_STARTED))

    fun getUserDetails(context: Context) {
        viewModelScope.launch {
            repository.getUserDetails(context) { response ->
                val parsedResponse = response?.data?.currentUser
                if (
                    parsedResponse?.id != null
                    && parsedResponse.slug != null
                    && parsedResponse.images != null
                ) {
                    val profileImageUrl: String? = parsedResponse.images.filter { image ->
                        image?.type == "profile"
                    }.getOrNull(0)?.url
                    val bannerImageUrl: String? = parsedResponse.images.filter { image ->
                        image?.type == "banner"
                    }.getOrNull(0)?.url
                    userDetails.value = Field(
                        UserDetails(
                            id = parsedResponse.id,
                            tag = parsedResponse.player?.gamerTag,
                            url = "${SITE_ENDPOINT}/${parsedResponse.slug}",
                            name = parsedResponse.name,
                            profileImageUrl = profileImageUrl,
                            bannerImageUrl = bannerImageUrl,
                            location = parsedResponse.location?.country
                        ), Status.SUCCESS
                    )
                } else {
                    userDetails.value = Field(null, Status.ERROR)
                }
            }
        }
    }
}