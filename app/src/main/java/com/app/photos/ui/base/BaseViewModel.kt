package com.app.photos.ui.base

import androidx.lifecycle.ViewModel
import com.app.photos.data.repository.BaseRepository

abstract class BaseViewModel(
    private val repository: BaseRepository
) : ViewModel() {

}