package com.tr.helpark.helparkcapstoneproject.features.search.data

import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.CompletableDeferred
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesRepository @Inject constructor(
    private val placesClient: PlacesClient
) {
    suspend fun findAutocompletePredictions(query: String): Result<List<AutocompletePrediction>> {

        val completableDeferred = CompletableDeferred<Result<List<AutocompletePrediction>>>()

        val request = FindAutocompletePredictionsRequest.builder()
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener {
            completableDeferred.complete(Result.Success(it.autocompletePredictions))
        }.addOnFailureListener {
            completableDeferred.complete(Result.Error(it))
        }

        return completableDeferred.await()
    }

    suspend fun fetchPlace(placeId: String): Result<Place> {

        val completableDeferred = CompletableDeferred<Result<Place>>()

        val placeFields = listOf(
            Place.Field.ID,
            Place.Field.LAT_LNG,
            Place.Field.OPENING_HOURS,
            Place.Field.ADDRESS,
            Place.Field.ICON_URL,
            Place.Field.NAME,
            Place.Field.PHONE_NUMBER,
            Place.Field.BUSINESS_STATUS
        )
        val request = FetchPlaceRequest.builder(placeId, placeFields).build()

        placesClient.fetchPlace(request)
            .addOnSuccessListener {
                completableDeferred.complete(Result.Success(it.place))
            }.addOnFailureListener {
                completableDeferred.complete(Result.Error(it))
            }
        return completableDeferred.await()
    }
}