package com.mirhoseini.redux

/**
 * Created by Mohsen on 24.07.17.
 */

private var store: Store<Action<CounterAction, Any>, State>? = null

fun main(args: Array<String>) {
    val store = Store(reduce, // reducer function
            State(0), // initial state
            Logger("Counter"))   // Middleware: logger

    val s1 = store.dispatch(Action(type = CounterAction.INCREMENT))
    val s2 = store.dispatch(Action(type = CounterAction.PLUS, value = 10))
}

object reduce : Store.Reducer<Action<CounterAction, Any>, State> {
    override fun reduce(action: Action<CounterAction, Any>, currentState: State): State {
        when (action.type) {
            CounterAction.INCREMENT -> return State(currentState.count + 1)
            CounterAction.PLUS -> return State(currentState.count + action.value as Int)
        }
        return currentState
    }
}

// Define store type
class State(val count: Int) {
    override fun toString(): String {
        return "count = " + this.count
    }
}

// Define action types
enum class CounterAction {
    INCREMENT,
    PLUS
}