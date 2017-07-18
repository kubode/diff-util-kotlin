package com.github.kubode.diffutilssample.util

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class AbstractViewHolder(
        parent: ViewGroup,
        @LayoutRes layoutRes: Int
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutRes, parent, false))
