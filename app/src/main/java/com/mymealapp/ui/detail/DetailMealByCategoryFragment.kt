package com.mymealapp.ui.detail

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
import com.mymealapp.R
import com.mymealapp.core.loadImage
import com.mymealapp.core.setYoutubeClickListener
import com.mymealapp.core.showToast
import com.mymealapp.data.model.Meal
import com.mymealapp.databinding.FragmentDetailMealByCategoryBinding
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
        onClickShareMeal()

        return binding.root
    }

    private fun setupShowMealDetail() {
        viewModel.fetchMealDetailsById(args.idMeal)
        viewModel.mealDetailLiveData.observe(viewLifecycleOwner) {
            if (it != null) {
                meal = it
                loadData(it)
                isMealFavorited()
                updateButtonIcon()
            }
        }
    }

    private fun loadData(meal: Meal) {
        with(binding) {
            try {
                loadImage(requireContext(), meal.image.toString(), imgAppBar)

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

                imgYoutubeWatchVideo.setYoutubeClickListener(meal.youtube.toString())

                isLoading(false)
            } catch (e: Exception) {
                showToast("${e.message}")
            }
        }
    }

    private fun isLoading(loading: Boolean) {
        with(binding) {
            progressBar.isVisible = loading
            txtInstructionTitle.isVisible = !loading
            txtCategory.isVisible = !loading
            txtArea.isVisible = !loading
            txtTags.isVisible = !loading
            txtInstructionDescriptions.isVisible = !loading
            fabFavorite.isVisible = !loading
            fabShare.isVisible = !loading
            imgYoutubeWatchVideo.isVisible = !loading
        }
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