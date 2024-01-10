package com.example.deamhome.data.datastore

import kotlinx.coroutines.flow.flow

class UserDataStore {
    val isLogin = flow { emit(true) }
}
