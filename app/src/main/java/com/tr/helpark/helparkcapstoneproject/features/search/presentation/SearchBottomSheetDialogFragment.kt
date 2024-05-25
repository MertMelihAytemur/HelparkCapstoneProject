package com.tr.helpark.helparkcapstoneproject.features.search.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tr.helpark.helparkcapstoneproject.R
import com.tr.helpark.helparkcapstoneproject.common.extensions.gone
import com.tr.helpark.helparkcapstoneproject.common.extensions.observeLiveData
import com.tr.helpark.helparkcapstoneproject.common.extensions.visible
import com.tr.helpark.helparkcapstoneproject.databinding.FragmentBottomSheetDialogSearchBinding
import com.tr.helpark.helparkcapstoneproject.features.main.MapsActivity
import com.tr.helpark.helparkcapstoneproject.features.search.data.model.PlacePredictionModel
import com.tr.helpark.helparkcapstoneproject.features.search.presentation.adapter.SearchLocationResultsAdapter

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchBottomSheetDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentBottomSheetDialogSearchBinding

    private val viewModel by viewModels<SearchViewModel>()

    private val searchLocationResultsAdapter by lazy {
        SearchLocationResultsAdapter(::getLocationFromSelectedSearchResult)
    }

   private var searchHistoryList: List<PlacePredictionModel> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBottomSheetDialogSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
        initUi()
        observeEvents()
    }


    private fun onSearchItemClick(place: PlacePredictionModel) {
        place.id?.let {
            if (isAdded) {
                dismiss()
                viewModel.saveSearchedPlace(place)
                place.latLng?.let { location ->
                    (activity as MapsActivity).setCurrentLocationAndMoveCamera(location)
                }
            }
        }
    }

    private fun initListeners() {
        binding.apply {
            ivCloseDialog.setOnClickListener {
                dismissNow()
            }

            svCarPark.apply {
                setTextChangeListener { searchKey ->
                    if (searchKey.isNotEmpty()) {
                        viewModel.findAutocompletePredictions(searchKey)
                    }else{
                        viewModel.cancelSearchJob()
                        showSearchHistory()
                    }
                }

                setCloseButtonActionListener {
                    viewModel.searchHistoryResultsLiveData.value?.let { showSearchHistory() }
                }
            }
        }
    }

    private fun initUi() {
        binding.apply {
            rvSearchResults.adapter = searchLocationResultsAdapter
        }
    }

    private fun observeEvents() {
        observeLiveData(viewModel.searchResultsLiveData) {
            handleSearchResult(it.isEmpty())
            searchLocationResultsAdapter.submitList(emptyList())
            searchLocationResultsAdapter.submitList(
                convertAutoCompletePredictionToPlacePredictionModel(it)
            )
        }

        observeLiveData(viewModel.fetchPlaceLiveData) {
            onSearchItemClick(
                PlacePredictionModel(
                    id = it?.id,
                    name = it?.name,
                    address = it?.address,
                    latLng = it?.latLng
                )
            )
        }

        observeLiveData(viewModel.searchHistoryResultsLiveData) {
            searchHistoryList = it
            showSearchHistory()
        }
    }

    private fun convertAutoCompletePredictionToPlacePredictionModel(list: List<AutocompletePrediction>): List<PlacePredictionModel> {
        return list.map {
            PlacePredictionModel(
                id = it.placeId,
                name = it.getFullText(null).toString(),
                latLng = null,
                address = it.getFullText(null).toString()
            )
        }
    }

    private fun handleSearchResult(isSearchListEmpty: Boolean) {
        binding.apply {
            svCarPark.hideKeyboard()
            tvSearchResults.text = getString(R.string.tv_search_results)
            layoutSearchNotFound.root.isVisible = isSearchListEmpty
            tvSearchResults.isVisible = !isSearchListEmpty
            rvSearchResults.isVisible = !isSearchListEmpty
        }
    }

    private fun showSearchHistory() {
        binding.apply {
            tvSearchResults.text = getString(R.string.search_history)
            layoutSearchNotFound.root.gone()
            rvSearchResults.visible()
            tvSearchResults.visible()
        }
        searchLocationResultsAdapter.submitList(emptyList())
        searchLocationResultsAdapter.submitList(searchHistoryList)
    }

    private fun getLocationFromSelectedSearchResult(placeId: String) {
        viewModel.fetchPlace(placeId)
    }
}