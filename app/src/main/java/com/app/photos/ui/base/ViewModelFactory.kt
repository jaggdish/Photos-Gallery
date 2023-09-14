package com.app.photos.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.photos.data.repository.BaseRepository
import com.app.photos.data.repository.UserRepository
import com.app.photos.ui.gallery.SharedViewModel

class ViewModelFactory(
    private val repository: BaseRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SharedViewModel::class.java) -> SharedViewModel(repository as UserRepository) as T
            else -> throw IllegalArgumentException("ViewModelClass not found")
        }
    }
}