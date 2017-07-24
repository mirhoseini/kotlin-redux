package com.mirhoseini.redux

class Logger<A, S>(private val tag: String) : Store.Middleware<A, S> {

    override fun dispatch(store: Store<A, S>, action: A, next: Store.NextDispatcher<A>?) {
        println(tag + "--> " + action.toString())
        next?.dispatch(action)
        println(tag + "<-- " + store.state.toString())
    }
}
