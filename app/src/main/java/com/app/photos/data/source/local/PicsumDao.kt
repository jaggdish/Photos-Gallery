package com.app.photos.data.source.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.app.photos.domain.model.PicsumPhotosItem
import kotlinx.coroutines.flow.Flow

@Dao
interface PicsumDao {

    @Query("SELECT * FROM photo")
    fun getAllPhotos(): Flow<List<PicsumPhotosItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun insertAll(photo: List<PicsumPhotosItem>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(photo: PicsumPhotosItem)

    @Query("DELETE FROM photo")
    suspend fun deleteAll()
}