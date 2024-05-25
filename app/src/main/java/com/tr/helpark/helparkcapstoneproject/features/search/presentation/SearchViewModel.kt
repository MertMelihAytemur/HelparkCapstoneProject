package com.tr.helpark.helparkcapstoneproject.features.search.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.Place
import com.tr.helpark.helparkcapstoneproject.common.manager.SearchHistoryManager
import com.tr.helpark.helparkcapstoneproject.features.search.data.PlacesRepository
import com.tr.helpark.helparkcapstoneproject.features.search.data.Result
import com.tr.helpark.helparkcapstoneproject.features.search.data.model.PlacePredictionModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val placesRepository: PlacesRepository,
    private val searchHistoryManager: SearchHistoryManager
) : ViewModel() {
    init {
        initSearchHistoryManager()
    }

    private val _searchResultsLiveData = MutableLiveData<List<AutocompletePrediction>>()
    val searchResultsLiveData: LiveData<List<AutocompletePrediction>> = _searchResultsLiveData

    private val _searchHistoryResultsLiveData = MutableLiveData<List<PlacePredictionModel>>()
    val searchHistoryResultsLiveData: LiveData<List<PlacePredictionModel>> = _searchHistoryResultsLiveData

    private val _fetchPlaceLiveData = MutableLiveData<Place?>()
    val fetchPlaceLiveData: LiveData<Place?> = _fetchPlaceLiveData

    private var searchQueryJob: Job? = null

    fun findAutocompletePredictions(query: String) {
        cancelSearchJob()
        searchQueryJob = viewModelScope.launch {
            delay(QUERY_DELAY)
            when (val result = placesRepository.findAutocompletePredictions(query)) {
                is Result.Error -> {
                    /*errorMessage.value = result.errorMessage
                    isLoading.value = false*/
                }

                is Result.Success -> {
                    _searchResultsLiveData.postValue(result.data.orEmpty())
                    //isLoading.value = false
                }

                Result.Loading -> {
                    //isLoading.value = true
                }
            }
        }
    }


    fun saveSearchedPlace(predictionModel: PlacePredictionModel) {
        viewModelScope.launch {
            searchHistoryManager.add(predictionModel)
        }
    }


    private fun initSearchHistoryManager() {
        viewModelScope.launch {
            searchHistoryManager.initialize { history ->
                val historyList = history.map {
                    it.copy(isHistory = true)
                }

                _searchHistoryResultsLiveData.postValue(historyList)
            }
        }
    }

    fun fetchPlace(placeId: String) {
        viewModelScope.launch {
            when (val result = placesRepository.fetchPlace(placeId)) {
                is com.tr.helpark.helparkcapstoneproject.features.search.data.Result.Error -> {
                   /* toastMessageState.value =
                        mutableMapOf("Konum Seçilemedi" to ToastMessageImageEnum.GENERAL_ERROR)*/
                }

                is Result.Success -> {
                    _fetchPlaceLiveData.postValue(result.data)
                }

                else -> {}
            }
        }
    }

    fun cancelSearchJob() = searchQueryJob?.cancel()

    override fun onCleared() {
        super.onCleared()
        searchHistoryManager.cancelScope()
    }

    companion object {
        private const val QUERY_DELAY = 500L
    }
}