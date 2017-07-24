package com.mirhoseini.redux

import java.util.*

class Store<A, S>(private val reducer: Reducer<A, S>, val initialState: S, vararg middlewares: Middleware<A, S>) {

    var state: S
        private set
    private val subscribers = ArrayList<Runnable>()
    private val next = ArrayList<NextDispatcher<A>>()

    init {
        this.state = initialState

        this.next.add(object : NextDispatcher<A> {
            override fun dispatch(action: A) {
                this@Store.dispatcher.dispatch(this@Store, action, null)
            }
        })
        for (i in middlewares.indices.reversed()) {
            val mw = middlewares[i]
            val n = next[0]
            next.add(0, object : NextDispatcher<A> {
                override fun dispatch(action: A) {
                    mw.dispatch(this@Store, action, n)
                }
            })
        }
    }

    interface Reducer<A, S> {
        fun reduce(action: A, currentState: S): S
    }

    interface Middleware<A, S> {
        fun dispatch(store: Store<A, S>, action: A, next: NextDispatcher<A>?)
    }

    interface NextDispatcher<A> {
        fun dispatch(action: A)
    }

    private val dispatcher = object : Middleware<A, S> {
        override fun dispatch(store: Store<A, S>, action: A, next: NextDispatcher<A>?) {
            synchronized(this) {
                state = store.reducer.reduce(action, state)
            }
            for (i in subscribers.indices) {
                store.subscribers[i].run()
            }
        }
    }

    fun dispatch(action: A): S? {
        this.next[0].dispatch(action)
        return this.state
    }

    fun subscribe(r: Runnable): Runnable {
        this.subscribers.add(r)
        return Runnable { subscribers.remove(r) }
    }
}
