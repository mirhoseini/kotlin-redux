package com.mirhoseini.redux

class Action<out T : Enum<*>, V>(val type: T, val value: V? = null) {

    override fun toString(): String {
        if (this.value != null) {
            return this.type.toString() + ": " + this.value.toString()
        } else {
            return this.type.toString()
        }
    }
}

