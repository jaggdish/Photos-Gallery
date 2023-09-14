package com.app.photos.data.repository

import com.app.photos.data.source.local.AppDatabase
import com.app.photos.data.source.local.PicsumDao
import com.app.photos.data.source.remote.PicsumApi

class UserRepository(
    private val api: PicsumApi,
    private val picsumDao: PicsumDao,
    private val db: AppDatabase? = null
) : BaseRepository() {

    suspend fun getPicsumPhotos() = safeApiCall {
        api.getPicsumPhotos()
    }
}