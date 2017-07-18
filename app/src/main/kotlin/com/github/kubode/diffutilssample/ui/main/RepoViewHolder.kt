package com.github.kubode.diffutilssample.ui.main

import android.view.ViewGroup
import android.widget.TextView
import com.github.kubode.diffutilssample.util.AbstractViewHolder
import com.github.kubode.diffutilssample.util.find

class RepoViewHolder(
        parent: ViewGroup
) : AbstractViewHolder(parent, android.R.layout.simple_list_item_2) {

    private val fullName = find<TextView>(android.R.id.text1)
    private val description = find<TextView>(android.R.id.text2)

    var repo: Item.Repo? = null
        set(value) {
            field = value
            fullName.text = value?.fullName
            description.text = value?.description
        }
}
