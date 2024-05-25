package com.tr.helpark.helparkcapstoneproject.common.manager

import com.helpark.helpark.common.utils.preferences.PreferencesKeys
import com.tr.helpark.helparkcapstoneproject.common.util.preferences.PreferencesManager
import com.tr.helpark.helparkcapstoneproject.core.di.IoDispatcher
import com.tr.helpark.helparkcapstoneproject.features.search.data.model.PlacePredictionModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.withContext
import java.util.LinkedList
import java.util.Queue
import javax.inject.Inject
import kotlin.reflect.typeOf

/**
 *
 * A class to manage search history on search screen. It uses
 * [PreferencesManager] to manage data.
 *
 * @param preferencesManager injected by hilt to manage SharedPreferences
 *
 */
class SearchHistoryManager @Inject constructor(
    private val preferencesManager: PreferencesManager,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {

    private lateinit var history: SearchHistoryModel

    private var isInitialized = false

    private val ioScope = CoroutineScope(
        ioDispatcher
    ) + SupervisorJob()


    // retrieves if any history data saved before
    fun initialize(onInitialized: (List<PlacePredictionModel>) -> Unit) {
        if (!isInitialized) {

            ioScope.launch {
                // gets history data from preferences if it is null
                // assigns empty model
                history = preferencesManager.getModel(
                    PreferencesKeys.KEY_SEARCHED_PLACES,
                    typeOf<SearchHistoryModel>()
                ) ?: SearchHistoryModel(LinkedList())
                // change property to make sure the method won't
                // run again
                isInitialized = true
                // invoke callback with current history data
                onInitialized(getHistory())
            }
        }
    }

    // it returns the updated list to
    suspend fun add(model: PlacePredictionModel): List<PlacePredictionModel> {
        if (!isInitialized)
            throw IllegalAccessException("Method accessed before initialization.")

        // Checks place id exists or not, if exists returns current history model
        val isPlaceExist = history.list.map { it.id }.contains(model.id)
        if (isPlaceExist)
            return getHistory()
        // add history data
        history.list.add(model)

        // remove if it exceeds limit
        if (history.list.size > HISTORY_LIMIT)
            history.list.remove()

        // puts the history model to preferences in IOScope
        withContext(ioScope.coroutineContext) {
            preferencesManager.putModel(PreferencesKeys.KEY_SEARCHED_PLACES, history)
        }

        return getHistory()
    }


    // it returns the data reversed because
    // the history property is a queue. Queue
    // works with FIFO (first in first out), but
    // the user should see latest search as first item
    // according to history's UX logic
    private fun getHistory(): List<PlacePredictionModel> =
        if (!isInitialized)
            throw IllegalAccessException("Method accessed before initialization.")
        else
            history.list.reversed()

    // history model to save on preferences
    data class SearchHistoryModel(
        // queue used because the history property should contain
        // only few items and it should pop the oldest item if it
        // reaches limit
        val list: Queue<PlacePredictionModel>
    )

    fun cancelScope() {
        ioScope.cancel()
    }

    companion object {
        private const val HISTORY_LIMIT = 5
    }
}