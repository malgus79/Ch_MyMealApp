package com.mymealapp.ui.fragment.detail

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mymealapp.R
import com.mymealapp.core.showToast
import com.mymealapp.databinding.FragmentDetailMealBinding
import com.mymealapp.model.data.Meal
import com.mymealapp.viewmodel.DetailMealViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailMealFragment : Fragment() {

    private lateinit var binding: FragmentDetailMealBinding
    private val viewModel: DetailMealViewModel by viewModels()

    private lateinit var meal: Meal
    private var isFavoriteMeal: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireArguments().let {
            DetailMealFragmentArgs.fromBundle(it).also { args ->
                meal = args.meal
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailMealBinding.inflate(inflater, container, false)

        showDetailMeal()
        showYoutubeVideo()
        isFavoriteMeal()
        onClickShareMeal()

        return binding.root
    }

    private fun showDetailMeal() {
        with(binding) {
            Glide.with(binding.root.context)
                .load(meal.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.gradient)
                .centerCrop()
                .into(imgAppBar)

            val category = "${getString(R.string.category_detail)} ${meal.category} "
            val area = "${getString(R.string.area_detail)} ${meal.area} "
            val tags = "${getString(R.string.tags_detail)} ${meal.tags} "
            toolbar.title = meal.name
            txtCategory.text = category
            txtArea.text = area
            txtInstructionDescriptions.text = meal.instructions

            if (meal.tags == null) {
                txtTags.text = getString(R.string.tags_no_data)
            } else {
                txtTags.text = tags
            }
        }
    }

    private fun showYoutubeVideo() {
        binding.imgYoutubeWatchVideo.setOnClickListener {
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(meal.youtube)))
            } catch (e: Exception) {
                showToast("${e.message}")
            }
        }
    }

    private fun isFavoriteMeal() {
        binding.fabFavorite.setOnClickListener {
            val isFavoriteMeal = isFavoriteMeal ?: return@setOnClickListener

            if (isFavoriteMeal) {
                showToast(getString(R.string.removed_meal))
            } else {
                showToast(getString(R.string.added_meal))
            }

            viewModel.saveOrDeleteFavoriteMeal(meal)
            this.isFavoriteMeal = !isFavoriteMeal
            updateButtonIcon()
        }
        viewLifecycleOwner.lifecycleScope.launch {
            isFavoriteMeal = viewModel.isMealFavorite(meal)
            updateButtonIcon()
        }
    }

    private fun updateButtonIcon() {
        isFavoriteMeal = isFavoriteMeal ?: return

        binding.fabFavorite.setImageResource(
            when {
                isFavoriteMeal!! -> R.drawable.ic_delete
                else -> R.drawable.ic_add
            }
        )
    }

    private fun onClickShareMeal() {
        binding.fabShare.setOnClickListener {
            try {
                val bitmapDrawable = binding.imgAppBar.drawable as BitmapDrawable
                val bitmap = bitmapDrawable.bitmap
                val bitmapPath =
                    MediaStore.Images.Media.insertImage(
                        context?.contentResolver,
                        bitmap,
                        "IMAGE" + System.currentTimeMillis(),
                        null
                    )
                val bitmapUri = Uri.parse(bitmapPath.toString())
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "image/*"
                intent.putExtra(Intent.EXTRA_STREAM, bitmapUri)
                startActivity(Intent.createChooser(intent, "My Meal App"))
            } catch (e: Exception) {
                showToast(getString(R.string.share_error))
            }
        }
    }
}