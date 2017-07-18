package com.github.kubode.diffutilssample.ui.common

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.kubode.diffutilssample.R
import com.github.kubode.diffutilssample.util.AbstractViewHolder
import com.github.kubode.diffutilssample.util.find

class RetryViewHolder(
        parent: ViewGroup,
        onRetry: () -> Unit
) : AbstractViewHolder(parent, R.layout.list_item_retry) {

    private val message = find<TextView>(R.id.message)
    private val retry = find<View>(R.id.retry)

    init {
        retry.setOnClickListener { onRetry() }
    }

    var throwable: Throwable? = null
        set(value) {
            field = value
            message.text = value?.message
        }
}
