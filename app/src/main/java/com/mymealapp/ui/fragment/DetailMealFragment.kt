package com.mymealapp.ui.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mymealapp.R
import com.mymealapp.core.showToast
import com.mymealapp.databinding.FragmentDetailMealBinding
import com.mymealapp.model.data.Meal
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailMealFragment : Fragment() {

    private lateinit var binding: FragmentDetailMealBinding

    private lateinit var meal: Meal

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

        return binding.root
    }

    private fun showDetailMeal() {
        Glide.with(binding.root.context)
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
        binding.txtTags.text = tags
        binding.txtInstructionDescriptions.text = meal.instructions
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
}