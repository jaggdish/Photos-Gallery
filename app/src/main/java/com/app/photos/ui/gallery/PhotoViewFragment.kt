package com.app.photos.ui.gallery

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dagger.hilt.android.scopes.FragmentScoped
import com.app.core.utlis.*
import com.app.photos.R
import com.app.photos.databinding.FragmentPhotoViewBinding

@FragmentScoped
class PhotoViewFragment : DialogFragment() {
    private lateinit var binding: FragmentPhotoViewBinding

    private var selectedPhoto: String? = null

    private val args: PhotoViewFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Material_Light_DialogWhenLarge_NoActionBar)
        dialog?.window?.navigationBarColor =
            ResourcesCompat.getColor(resources, R.color.black, null)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoViewBinding.inflate(inflater, container, false)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        args.selectedPhoto?.let {
            selectedPhoto = it
        }

        binding.imageButtonBack.setOnClickListener {
            //findNavController().popBackStack()
            findNavController().navigateUp()
        }

        /**
         * Case 1: [Glide] will load images from Picsum API - main case
         *
         * ### Exception on saving photo -
         * 1. If device has write permission then save it through bitmap otherwise ask for [hasStoragePermission]
         * 2. If Glide takes time to load or failed to load photo then the photo can not be save,
         * the photo is converting to bitmap which will through null object exception. To handle this,
         * [GlideListenerImpl] Object class is created to handle Glide event listener
         * 3. The photo offline caching [DiskCacheStrategy] enable, if the photo is not loaded and
         * device has not connected the the image download and share option is set [isVisible] false.
         */

        Glide.with(requireContext()).load(selectedPhoto).diskCacheStrategy(DiskCacheStrategy.ALL)
            .listener(GlideListenerImpl.OnCompleted {

                binding.progressBar.isVisible = false
                showToast(getString(R.string.zoom_in_out))

            }).into(binding.zoomViewPhoto)
    }
}