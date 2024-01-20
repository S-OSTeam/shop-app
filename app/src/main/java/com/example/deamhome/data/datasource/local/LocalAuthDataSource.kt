package com.example.deamhome.data.datasource.local

import androidx.datastore.core.DataStore
import com.example.deamhome.data.model.response.Token
import com.example.deamhome.data.model.response.Token.Companion.EMPTY
import com.example.deamhome.data.model.response.Token.Companion.EMPTY_TOKEN
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class LocalAuthDataSource(private val dataStore: DataStore<Token>) {
    val isLogin: Flow<Boolean> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(EMPTY_TOKEN)
            } else {
                throw exception
            }
        }.map {
            it.accessToken != EMPTY
        }

    val token: Flow<Token> = dataStore.data
        .catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            if (exception is IOException) {
                emit(EMPTY_TOKEN)
            } else {
                throw exception
            }
        }

    suspend fun updateToken(newToken: Token) {
        dataStore.updateData {
            newToken
        }
    }

    suspend fun removeToken() {
        dataStore.updateData {
            EMPTY_TOKEN
        }
    }

    companion object {
        const val AUTH_TOKEN_STORE_NAME = "auth_token_data_store.json"
    }
}
