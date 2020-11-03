package com.android.tutorial.sample_api_listing.util

import android.content.Context
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.android.tutorial.sample_api_listing.R


private var animations: IntArray =
        intArrayOf(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)

fun Fragment.show(activity: FragmentActivity) {
    activity
        .supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(animations[0],animations[1],animations[2],animations[3])
        .add(R.id.container, this, this.javaClass.name)
        .addToBackStack(this.javaClass.name)
        .commit()
}

fun Fragment.init(activity: FragmentActivity) {
    activity
        .supportFragmentManager
        .beginTransaction()
        .setCustomAnimations(animations[0], animations[1], animations[2], animations[3])
        .add(R.id.container, this)
        .commit()
}

fun ImageView.setDrawable(context: Context, res: Int) {
    this.setImageDrawable(ContextCompat.getDrawable(context, res))
}