package com.github.kubode.diffutilssample.ui.main

import com.github.kubode.diffutilssample.github.entity.Repo
import redux.api.Reducer

data class State(
        val repos: List<Repo> = emptyList(),
        val hasMoreItems: Boolean = true,
        val isLoading: Boolean = false,
        val isAppending: Boolean = false,
        val error: Throwable? = null) {

    val isAppendable = hasMoreItems && !isLoading && !isAppending
}

sealed class Action {
    class LoadStart : Action()
    data class LoadSuccess(val result: List<Repo>) : Action()
    data class LoadError(val throwable: Throwable) : Action()
    class AppendStart : Action()
    data class AppendSuccess(val result: List<Repo>) : Action()
    data class AppendError(val throwable: Throwable) : Action()
}

val reducer = Reducer<State> { state: State, action: Any ->
    when (action) {
        is Action.LoadStart -> state.copy(
                isLoading = true,
                error = null)
        is Action.LoadSuccess -> state.copy(
                repos = action.result,
                hasMoreItems = action.result.isNotEmpty(),
                isLoading = false)
        is Action.LoadError -> state.copy(
                isLoading = false,
                error = action.throwable)
        is Action.AppendStart -> state.copy(
                isAppending = true,
                error = null)
        is Action.AppendSuccess -> state.copy(
                repos = state.repos + action.result,
                hasMoreItems = action.result.isNotEmpty(),
                isAppending = false)
        is Action.AppendError -> state.copy(
                isAppending = false,
                error = action.throwable)
        else -> state
    }
}
