package com.github.kubode.diffutilssample.github

import com.github.kubode.diffutilssample.github.entity.Repo
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApi {

    @GET("/repositories")
    fun repositories(@Query("since") since: String? = null): Single<List<Repo>>
}
