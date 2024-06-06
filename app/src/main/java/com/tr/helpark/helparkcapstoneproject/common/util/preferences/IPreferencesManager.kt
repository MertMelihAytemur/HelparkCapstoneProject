package com.tr.helpark.helparkcapstoneproject.common.util.preferences

import kotlin.reflect.KType

interface IPreferencesManager {

    fun getBoolean(key: String, defaultValue: Boolean = false): Boolean
    fun getInt(key: String, defaultValue: Int = 0): Int
    fun getString(key: String, defaultValue: String? = null): String?
    fun getLong(key: String, defaultValue: Long = 0L): Long
    fun getStringSet(key: String, defaultValue: Set<String>? = null): Set<String>?
    fun getFloat(key: String, defaultValue: Float = 0.0f): Float
    fun getDouble(key: String, defaultValue: Double = 0.0): Double
    fun contains(key: String): Boolean

    /**
     * Gets a model from the preferences by parsing
     * it using GSON. The model fields should
     * include @SerializedName.
     *
     * To retrieve the model, you can use such as:
     *
     * val model: List<Item> = manager.getModel("key", typeOf<List<Item>>())
     *
     * The type prefix is required, unfortunately it is not possible to use it
     * like:
     *
     * val model = manager.getModel("key", typeOf<List<Item>>())
     *
     * It always requires the type, a.k.a "List<Item>" unless the default value
     * is specified such as "listOf<Item>()".
     */
    suspend fun <T: Any> getModel(key: String, type: KType, defaultValue: T? = null): T?

    fun putBoolean(key: String, value: Boolean)
    fun putString(key: String, value: String?)
    fun putInt(key: String, value: Int)
    fun putStringSet(key: String, value: Set<String>)
    fun putLong(key: String, value: Long)
    fun putFloat(key: String, value: Float)
    fun putDouble(key: String, value: Double)

    /**
     * Puts a model to the preferences by parsing
     * it using GSON. The model fields should
     * include @SerializedName.
     */
    suspend fun <T: Any> putModel(key: String, model: T)
    fun remove(key: String)
    fun clear()
}