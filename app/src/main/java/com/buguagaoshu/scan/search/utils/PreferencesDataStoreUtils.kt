package com.buguagaoshu.scan.search.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// 定义 DataStore 名称
private const val PREFERENCES_DATA_STORE_NAME = "SCAN_SEARCH"

// 创建 Preferences DataStore 实例
private val Context.preferencesDataStore: DataStore<Preferences> by preferencesDataStore(name = PREFERENCES_DATA_STORE_NAME)

object PreferencesDataStoreUtils {

    // 保存字符串数据
    suspend fun saveString(context: Context, key: String, value: String) {
        val dataStoreKey = stringPreferencesKey(key)
        context.preferencesDataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    // 读取字符串数据
    fun readString(context: Context, key: String): Flow<String?> {
        val dataStoreKey = stringPreferencesKey(key)
        return context.preferencesDataStore.data.map { preferences ->
            preferences[dataStoreKey]
        }
    }

    // 更多类型的保存和读取方法可以按需添加，例如保存和读取 Int、Boolean 等
    suspend fun saveInt(context: Context, key: String, value: Int) {
        val dataStoreKey = androidx.datastore.preferences.core.intPreferencesKey(key)
        context.preferencesDataStore.edit { preferences ->
            preferences[dataStoreKey] = value
        }
    }

    fun readInt(context: Context, key: String): Flow<Int?> {
        val dataStoreKey = androidx.datastore.preferences.core.intPreferencesKey(key)
        return context.preferencesDataStore.data.map { preferences ->
            preferences[dataStoreKey]
        }
    }
}