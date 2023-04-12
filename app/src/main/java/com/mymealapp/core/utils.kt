package com.mymealapp.core

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
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

fun View.setYoutubeClickListener(youtubeUrl: String) {
    setOnClickListener {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeUrl))
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

fun Activity.showConnectivitySnackbar(isConnected: Boolean) {
    val binding = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
    val messageRes = if (isConnected) R.string.connection_restored else R.string.no_connection
    val duration = if (isConnected) Snackbar.LENGTH_SHORT else Snackbar.LENGTH_INDEFINITE
    val backgroundColorRes = if (isConnected) R.color.green_normal_theme else R.color.red_theme
    val snackbar = Snackbar.make(findViewById(android.R.id.content), messageRes, duration)
        .setAnchorView(binding)
        .setBackgroundTint(ContextCompat.getColor(this, backgroundColorRes))
    if (!isConnected) {
        snackbar.setAction(getString(R.string.rule_out)) {}
    }
    snackbar.show()
}