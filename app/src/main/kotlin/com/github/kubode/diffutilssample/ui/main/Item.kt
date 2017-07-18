package com.github.kubode.diffutilssample.ui.main

import com.github.kubode.diffutilssample.util.Diffable

sealed class Item(val viewType: Int) : Diffable {

    companion object {
        const val TYPE_REPO = 0
        const val TYPE_LOADING = 1
        const val TYPE_RETRY = 2
    }

    data class Repo(
            val id: Long,
            val fullName: String,
            val description: String?
    ) : Item(TYPE_REPO) {
        override fun isTheSame(other: Diffable) = (id == (other as? Repo)?.id)
    }

    object Loading : Item(TYPE_LOADING)

    data class Retry(val throwable: Throwable) : Item(TYPE_RETRY)
}
