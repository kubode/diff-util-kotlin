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

    private fun State.toItems(): List<Item> {
        val items = arrayListOf<Item>()
        items += repos.map { Item.Repo(it.id, it.fullName, it.description) }
        if (isLoading || isAppending) items += Item.Loading
        error?.let { items += Item.Retry(it) }
        return items
    }

    var state: State = initialState
        set(value) {
            field = value
            this.items = value.toItems()
        }

    private var items: List<Item> by Delegates.observable(initialState.toItems()) { _, old, new ->
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
