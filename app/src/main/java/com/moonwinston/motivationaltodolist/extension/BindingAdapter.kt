package com.moonwinston.motivationaltodolist.extension

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

@BindingAdapter("bind:background")
fun setBackgroundColor(view: View, colorId: Int) {
    view.setBackgroundColor(view.context.resources.getColor(colorId))
}

@BindingAdapter("bind:adapter")
fun setAdapter(view: RecyclerView, baseAdapter: RecyclerView.Adapter<*>) {
    view.adapter = baseAdapter
}