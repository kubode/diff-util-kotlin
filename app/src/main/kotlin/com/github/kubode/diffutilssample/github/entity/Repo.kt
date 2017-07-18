package com.github.kubode.diffutilssample.github.entity

import com.google.gson.annotations.SerializedName

data class Repo(
        @SerializedName("id") val id: Long,
        @SerializedName("full_name") val fullName: String,
        @SerializedName("description") val description: String?,
        @SerializedName("url") val url: String
) {
    override fun toString() = fullName
}
