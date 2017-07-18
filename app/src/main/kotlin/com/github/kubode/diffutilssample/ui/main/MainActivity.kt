package com.github.kubode.diffutilssample.ui.main

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import com.github.kubode.diffutilssample.R
import com.github.kubode.diffutilssample.github.GitHubApi
import com.github.kubode.diffutilssample.util.find
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import redux.api.Store
import redux.api.enhancer.Middleware
import redux.applyMiddleware
import redux.createStore
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    // Redux
    private val store: Store<State> = createStore(
            reducer,
            State(),
            applyMiddleware(Middleware<State> { store, next, action ->
                val before = store.state
                next.dispatch(action)
                val after = store.state
                Log.v(TAG, "$before -> $after")
            }))

    // API
    private val api: GitHubApi = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GitHubApi::class.java)

    private fun load() {
        store.dispatch(Action.LoadStart())
        api.repositories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { store.dispatch(Action.LoadSuccess(it)) },
                        { store.dispatch(Action.LoadError(it)) })
    }

    private fun append() {
        store.dispatch(Action.AppendStart())
        api.repositories(store.state.repos.lastOrNull()?.id?.toString())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { store.dispatch(Action.AppendSuccess(it)) },
                        { store.dispatch(Action.AppendError(it)) })
    }

    // Main
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        load()
        initializeViews()
    }

    private val toolbar by lazy { find<Toolbar>(R.id.toolbar) }
    private val recyclerView by lazy { find<RecyclerView>(R.id.recyclerView) }

    private val adapter by lazy {
        MainAdapter(
                initialState = store.state,
                onRetry = { load() })
    }

    fun initializeViews() {
        setSupportActionBar(toolbar)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                // Append if the last item is visible.
                val state = store.state
                if (!state.isAppendable) return
                val lastChild = recyclerView.getChildAt(recyclerView.childCount - 1) ?: return
                val lastViewHolder = recyclerView.getChildViewHolder(lastChild) ?: return
                if (lastViewHolder.adapterPosition != recyclerView.adapter.itemCount - 1) return
                append()
            }
        })
    }

    private lateinit var subscription: Store.Subscription

    override fun onStart() {
        super.onStart()
        drawViews(store.state)
        subscription = store.subscribe { drawViews(store.state) }
    }

    private fun drawViews(state: State) {
        adapter.state = state
    }

    override fun onStop() {
        subscription.unsubscribe()
        super.onStop()
    }
}
