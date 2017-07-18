package com.github.kubode.diffutilssample.util

import android.app.Activity
import android.support.annotation.IdRes
import android.support.v7.widget.RecyclerView
import android.view.View

fun <V : View?> Activity.find(@IdRes id: Int): V = findViewById<V>(id)
fun <V : View?> RecyclerView.ViewHolder.find(@IdRes id: Int): V = itemView.findViewById<V>(id)
