# DiffUtilSample

A sample of [DiffUtil](https://developer.android.com/reference/android/support/v7/util/DiffUtil.html) written in Kotlin.

# Data flow

1. Subscribe to state changes. (MainActivity.onStart)
1. When notified state changes, set new state to adapter. (MainActivity.onStart)
1. Transform new state to item list and set to `items`. (MainAdapter.state)
1. Calculate item list differences and dispatch updates to adapter. (MainAdapter.items)

# How it works

## Diffable

`Diffable` is an interface for simplify use `DiffUtil.Callback`.

### Diffable.isTheSame

`Diffable.isTheSame` returns true if `this` and `other` has same id.
Default implementation returns `equals`.
`data class` should override this.

like this:
```kotlin
override fun isTheSame(other: Diffable) = (id == (other as? Repo)?.id)
```

### Diffable.isContentsTheSame

`Diffable.isContentsTheSame` returns true if `this` equals to `other`.
Default implementation returns `equals`.
