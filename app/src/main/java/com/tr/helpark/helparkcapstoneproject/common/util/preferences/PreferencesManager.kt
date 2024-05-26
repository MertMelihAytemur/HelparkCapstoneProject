package com.tr.helpark.helparkcapstoneproject.common.util.preferences

import android.content.Context
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.lang.Double.doubleToRawLongBits
import java.lang.Double.longBitsToDouble
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KType
import kotlin.reflect.javaType


class PreferencesManager @Inject constructor(
    @ApplicationContext context: Context,
    private val gsonProvider: Provider<Gson>
) : IPreferencesManager {

    /**
     * The core preferences elements.
     */
    private val preferences = context.getSharedPreferences(preferencesName, Context.MODE_PRIVATE)
    private var edit = preferences.edit()


    /**
     * The GSON instance (getter)
     */
    private val gson: Gson
        get() = gsonProvider.get()

    override fun getBoolean(key: String, defaultValue: Boolean): Boolean =
        preferences.getBoolean(key, defaultValue)


    override fun getInt(key: String, defaultValue: Int): Int =
        preferences.getInt(key, defaultValue)

    override fun getString(key: String, defaultValue: String?): String? =
        preferences.getString(key, defaultValue)

    override fun getLong(key: String, defaultValue: Long): Long =
        preferences.getLong(key, defaultValue)

    override fun getStringSet(key: String, defaultValue: Set<String>?): Set<String>? =
        preferences.getStringSet(key, defaultValue)

    override fun getFloat(key: String, defaultValue: Float): Float =
        preferences.getFloat(key, defaultValue)

    override fun getDouble(key: String, defaultValue: Double): Double {
        if (!preferences.contains(key))
            return defaultValue

        // Doubles are stored as longs, should be parsed
        // as doubles.
        return longBitsToDouble(getLong(key))
    }

    override fun contains(key: String): Boolean =
        preferences.contains(key)

    @OptIn(ExperimentalStdlibApi::class)
    override suspend fun <T : Any> getModel(key: String, type: KType, defaultValue: T?): T? {
        val item = {
            // Query the string from the preferences first.
            // Otherwise, return null.
            getString(key)?.let { data ->
                // Using the type provided,
                runCatching {
                    gson.fromJson(data, type.javaType) as? T
                }.onFailure { throwable ->
                    Timber.tag(TAG).d(throwable)
                }.getOrNull() ?: defaultValue
            }
        }

        // Use the io thread to parse the items.
        return withContext(Dispatchers.IO) {
            return@withContext item()
        }
    }

    override fun putBoolean(key: String, value: Boolean) =
        edit.putBoolean(key, value).apply()

    override fun putString(key: String, value: String) =
        edit.putString(key, value).apply()


    override fun putInt(key: String, value: Int) =
        edit.putInt(key, value).apply()


    override fun putStringSet(key: String, value: Set<String>) =
        edit.putStringSet(key, value).apply()

    override fun putLong(key: String, value: Long) =
        edit.putLong(key, value).apply()

    override fun putFloat(key: String, value: Float) =
        edit.putFloat(key, value).apply()

    override fun putDouble(key: String, value: Double) {
        val longEquivalence = doubleToRawLongBits(value)
        edit.putLong(key, longEquivalence).apply()
    }

    /**
     * Puts a model to the preferences by parsing
     * it using GSON. The model fields should
     * include @SerializedName.
     */
    override suspend fun <T : Any> putModel(key: String, model: T) {
        withContext(Dispatchers.IO) {
            edit.putString(key, gson.toJson(model)).apply()
        }
    }

    override fun remove(key: String) =
        edit.remove(key).apply()

    override fun clear() =
        edit.clear().apply()

    companion object {
        private const val preferencesName = "travel_info_app_preferences"

        private const val TAG = "app-preferences"
    }
}