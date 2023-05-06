package net.kerner.entkerner.abstract

abstract class AbstractFilterer<T> {
    abstract val name: String
    abstract fun filter(t: T): Boolean
}