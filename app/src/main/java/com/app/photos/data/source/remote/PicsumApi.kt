package com.app.photos.data.source.remote

import com.app.core.utlis.Constants
import com.app.photos.domain.model.PicsumPhotoList
import retrofit2.http.GET
import retrofit2.http.Query

interface PicsumApi {
   /* @GET("list/")
    suspend fun getPicsumPhotos(@Query("page") page: Int = pageNo,
        @Query("limit") limit: Int = Constants.API.PHOTO_LIMIT
    ): PicsumPhotoList*/

    @GET("list/")
    suspend fun getPicsumPhotos(@Query("page") page: Int = Constants.API.PAGE_NO, @Query("limit") limit: Int = Constants.API.PHOTO_LIMIT): PicsumPhotoList
}