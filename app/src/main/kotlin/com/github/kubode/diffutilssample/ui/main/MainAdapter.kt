package com.github.kubode.diffutilssample.ui.main

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.ViewGroup
import com.github.kubode.diffutilssample.ui.common.LoadingViewHolder
import com.github.kubode.diffutilssample.ui.common.RetryViewHolder
import com.github.kubode.diffutilssample.util.AbstractViewHolder
import com.github.kubode.diffutilssample.util.calculateDiff
import kotlin.properties.Delegates

class MainAdapter(
        initialState: State,
        private val onRetry: () -> Unit
) : RecyclerView.Adapter<AbstractViewHolder>() {

    var state: State = initialState
        set(value) {
            field = value
            val items = arrayListOf<Item>()
            items += value.repos.map { Item.Repo(it.id, it.fullName, it.description) }
            if (value.isLoading || value.isAppending) items += Item.Loading
            value.error?.let { items += Item.Retry(it) }
            this.items = items
        }

    private var items: List<Item> by Delegates.observable(emptyList()) { _, old, new ->
        val start = System.currentTimeMillis()
        calculateDiff(old, new).dispatchUpdatesTo(this)
        Log.v("MainAdapter", "calculateDiff and dispatchUpdatesTo finished in ${System.currentTimeMillis() - start}ms.")
    }

    override fun getItemCount() = items.count()

    override fun getItemViewType(position: Int) = items[position].viewType

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbstractViewHolder {
        return when (viewType) {
            Item.TYPE_REPO -> RepoViewHolder(parent)
            Item.TYPE_LOADING -> LoadingViewHolder(parent)
            Item.TYPE_RETRY -> RetryViewHolder(parent, onRetry)
            else -> throw IllegalArgumentException("viewType: $viewType")
        }
    }

    override fun onBindViewHolder(holder: AbstractViewHolder, position: Int) {
        val item = items[position]
        when {
            item is Item.Repo && holder is RepoViewHolder -> {
                holder.repo = item
            }
            item is Item.Retry && holder is RetryViewHolder -> {
                holder.throwable = item.throwable
            }
        }
    }
}
