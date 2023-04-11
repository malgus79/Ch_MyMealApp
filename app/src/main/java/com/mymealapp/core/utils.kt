package com.mymealapp.core

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.mymealapp.R
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}

inline fun <T : View> T.showIf(condition: (T) -> Boolean) {
    if (condition(this)) {
        show()
    } else {
        hide()
    }
}

inline fun <T : View> T.hideIf(condition: (T) -> Boolean) {
    if (condition(this)) {
        hide()
    } else {
        show()
    }
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Fragment.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    requireContext().showToast(message, duration)
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun RecyclerView.setupRecyclerView(
    adapter: RecyclerView.Adapter<*>,
    columns: Int,
    itemAnimator: RecyclerView.ItemAnimator,
    vertical: Boolean
) {
    this.apply {
        this.adapter = ScaleInAnimationAdapter(adapter)
        this.itemAnimator = itemAnimator.apply { addDuration = 500 }
        this.layoutManager = StaggeredGridLayoutManager(
            columns,
            if (vertical) StaggeredGridLayoutManager.VERTICAL else StaggeredGridLayoutManager.HORIZONTAL
        )
        setHasFixedSize(true)
        show()
    }
}

fun loadImage(context: Context, url: String, imageView: ImageView) {
    Glide.with(context)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .error(R.drawable.gradient)
        .centerCrop()
        .into(imageView)
}