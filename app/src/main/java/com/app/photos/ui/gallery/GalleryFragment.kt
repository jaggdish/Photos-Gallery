package com.app.photos.ui.gallery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.scopes.FragmentScoped
import com.app.core.utlis.Constants
import com.app.core.utlis.snakbar
import com.app.photos.App
import com.app.photos.R
import com.app.photos.data.repository.UserRepository
import com.app.photos.data.source.remote.PicsumApi
import com.app.photos.data.util.DataState
import com.app.photos.databinding.FragmentGalleryBinding
import com.app.photos.domain.model.PicsumPhotosItem
import com.app.photos.ui.adapter.GalleryAdapter
import com.app.photos.ui.base.BaseFragment


@FragmentScoped
class GalleryFragment :
    BaseFragment<SharedViewModel, FragmentGalleryBinding, UserRepository>() {

    private lateinit var galleryAdapter: GalleryAdapter

    private var photoListItem: MutableList<PicsumPhotosItem>? = null

    private var isPhotosLoaded = false
    private var isFirstCall = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        photoListItem = mutableListOf()
        Constants.API.PAGE_NO = 1
        Log.e("TAG", "onCreate: "+Constants.API.PAGE_NO)

        isFirstCall = true
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        galleryAdapter = GalleryAdapter {
            val action = GalleryFragmentDirections.actionGalleryFragmentToPhotoViewFragment()

            action.selectedPhoto = it.download_url
            findNavController().navigate(action)
        }

        binding.recycleViewPhotoList.adapter = galleryAdapter
        galleryAdapter.stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.recycleViewPhotoList.setHasFixedSize(true)

        val layoutManager = binding.recycleViewPhotoList.layoutManager as LinearLayoutManager

        if(isFirstCall) {
            getPhotos()
        }

        var pastItemsVisible: Int
        var visibleItemCount: Int
        var totalItemCount: Int
        binding.recycleViewPhotoList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = layoutManager.childCount
                    totalItemCount = layoutManager.itemCount
                    pastItemsVisible = layoutManager.findFirstVisibleItemPosition()
                    if (!binding.progressBar.isVisible) {
                        if (visibleItemCount + pastItemsVisible >= totalItemCount) {
                            isPhotosLoaded = false
                            getPhotos()
                        }
                    }
                }
            }
        })
        viewModel.photoList.observe(viewLifecycleOwner) {

            when (it) {
                is DataState.Success -> {
                    photoListItem?.addAll(it.value)
                    Log.e("TAG", "onViewCreated: "+ (photoListItem?.get(0)))

                    Log.e("TAG", "onAPICall: "+Constants.API.PAGE_NO)
                    updateUI(photoListItem)  //success update result

                    //photoListItem = it.value
                    binding.progressBar.isVisible = false
                    isFirstCall = false
                    isPhotosLoaded = true
                    Constants.API.PAGE_NO = Constants.API.PAGE_NO+1

                }

                is DataState.Loading -> {
                    binding.progressBar.isVisible = true
                    isPhotosLoaded = false
                }

                is DataState.Failure -> {
                    binding.progressBar.isVisible = false
                    binding.root.snakbar(getString(R.string.no_internet_connection))
                    isPhotosLoaded = false
                }
            }
        }

        binding.imageButtonRefresh.setOnClickListener {
            isPhotosLoaded = false
            Constants.API.PAGE_NO = 1
            getPhotos()
        }
    }

    private fun updateUI(photoListItem: List<PicsumPhotosItem>?) {
        galleryAdapter.submitList(photoListItem?.toMutableList())
    }

    private fun getPhotos() {
        if(!isPhotosLoaded) {
            viewModel.photoData()
        }
    }

    override fun getViewModel() = SharedViewModel::class.java

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentGalleryBinding.inflate(inflater, container, false)

    override fun getFragmentRepository(): UserRepository {
        val api = remoteDataSource.buildApi(PicsumApi::class.java)
        val picsumDao = (requireActivity().applicationContext as App).database.PicsumDao()
        return UserRepository(api, picsumDao)
    }
}