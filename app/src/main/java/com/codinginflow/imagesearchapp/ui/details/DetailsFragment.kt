package com.codinginflow.imagesearchapp.ui.details

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.codinginflow.imagesearchapp.R
import com.codinginflow.imagesearchapp.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val args: DetailsFragmentArgs by navArgs()

    private var _binding: FragmentDetailsBinding? = null
    private val binding: FragmentDetailsBinding
        get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentDetailsBinding.bind(view)

        val photo = args.photo

        binding.apply {
            Glide.with(this@DetailsFragment)
                .load(photo.urls.full)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progressBar.isVisible = false
                        textViewCreator.isVisible = true
                        textViewImageDescription.isVisible = photo.description != null
                        return false
                    }
                })
                .into(image)

            textViewImageDescription.text = photo.description

            textViewCreator.apply {
                text = getString(R.string.text_view_text_creator, photo.user.name)
                paint.isUnderlineText = true
                val intent = Intent(Intent.ACTION_VIEW, photo.user.attributionUrl.toUri())
                setOnClickListener {
                    startActivity(intent)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}