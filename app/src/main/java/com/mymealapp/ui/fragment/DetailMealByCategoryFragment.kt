package com.mymealapp.ui.fragment

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mymealapp.R
import com.mymealapp.core.showToast
import com.mymealapp.databinding.FragmentDetailMealByCategoryBinding
import com.mymealapp.model.data.Meal
import com.mymealapp.viewmodel.DetailMealViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailMealByCategoryFragment : Fragment() {

    private lateinit var binding: FragmentDetailMealByCategoryBinding
    private val args by navArgs<DetailMealByCategoryFragmentArgs>()
    private val viewModel: DetailMealViewModel by viewModels()

    private lateinit var meal: Meal
    private var isFavoriteMeal: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailMealByCategoryBinding.inflate(inflater, container, false)
        meal = Meal("", "", "", "", "", "")

        isLoading(true)
        setupShowMealDetail()
        isMealFavorited()
        updateButtonIcon()
        onClickShareMeal()

        return binding.root
    }

    private fun setupShowMealDetail() {
        viewModel.fetchMealDetailsById(args.idMeal)
        viewModel.mealDetailLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                meal = it
                loadData(it)
            }
        }
    }

    private fun loadData(meal: Meal) {
        try {
            Glide.with(this@DetailMealByCategoryFragment)
                .load(meal.image)
                .transition(DrawableTransitionOptions.withCrossFade())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.gradient)
                .centerCrop()
                .into(binding.imgAppBar)

            val category = "${getString(R.string.category_detail)} ${meal.category} "
            val area = "${getString(R.string.area_detail)} ${meal.area} "
            val tags = "${getString(R.string.tags_detail)} ${meal.tags} "
            binding.toolbar.title = meal.name
            binding.txtCategory.text = category
            binding.txtArea.text = area
            binding.txtInstructionDescriptions.text = meal.instructions

            if (meal.tags == null) {
                binding.txtTags.text = getString(R.string.tags_no_data)
            } else { binding.txtTags.text = tags }

            binding.imgYoutubeWatchVideo.setOnClickListener {
                try {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(meal.youtube)))
                } catch (e: Exception) {
                    showToast("${e.message}")
                }
            }

            isLoading(false)
        } catch (e: Exception) {
            showToast("${e.message}")
        }


    }

    private fun isLoading(loading: Boolean) {
        binding.progressBar.isVisible = loading
        binding.txtInstructionTitle.isVisible = !loading
        binding.txtCategory.isVisible = !loading
        binding.txtArea.isVisible = !loading
        binding.txtInstructionDescriptions.isVisible = !loading
    }

    private fun isMealFavorited() {
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