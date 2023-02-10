package com.mymealapp.ui.fragment.carousel


/**  Option Carousel:

private fun setupRandomMeals() {
viewModel.fetchRandomMeal().observe(viewLifecycleOwner) {
when (it) {
is Resource.Loading -> {
binding.progressBar.show()
}
is Resource.Success -> {
binding.progressBar.hide()
if (it.data.meals.isEmpty()) {
binding.imgRandomMeal.hide()
return@observe
}
setupShowRandomMeals(it.data.meals.first())
}
is Resource.Failure -> {
binding.progressBar.hide()
showToast(getString(R.string.error_detail) + it.exception)
}
}
}
}

private fun setupShowRandomMeals(item: Meal) {
Glide.with(binding.root.context)
.load(item.image)
.transition(DrawableTransitionOptions.withCrossFade())
.diskCacheStrategy(DiskCacheStrategy.ALL)
.error(R.drawable.gradient)
.centerCrop()
.into(binding.imgRandomMeal)

binding.cvHomeRandom.setOnClickListener {
findNavController().navigate(
HomeFragmentDirections.actionHomeFragmentToMealDetailFragment(item)
)
}
}

 **/
