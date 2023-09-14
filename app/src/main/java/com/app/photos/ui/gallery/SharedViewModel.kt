package com.app.photos.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.scopes.ViewModelScoped
import com.app.photos.data.repository.UserRepository
import com.app.photos.data.util.DataState
import com.app.photos.domain.model.PicsumPhotoList
import com.app.photos.ui.base.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@ViewModelScoped
class SharedViewModel @Inject constructor(
    private val repository: UserRepository
) : BaseViewModel(repository) {

    private val _photoList: MutableLiveData<DataState<PicsumPhotoList>> = MutableLiveData()
    val photoList: LiveData<DataState<PicsumPhotoList>>
        get() = _photoList

    fun photoData() = viewModelScope.launch {
        _photoList.value = DataState.Loading
        _photoList.value = repository.getPicsumPhotos()
    }
}