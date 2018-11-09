package se.gustavkarlsson.koptional

sealed class Optional<out T : Any> {

    abstract val value: T?

    abstract val unsafeValue: T

    abstract val isPresent: Boolean

    abstract val isAbsent: Boolean

    abstract fun ifPresent(consumer: (T) -> Unit): Optional<T>

    abstract fun ifAbsent(action: () -> Unit): Optional<T>

    abstract fun filter(predicate: (T) -> Boolean): Optional<T>

    abstract fun <R : Any> map(mapper: (T) -> R?): Optional<R>

    abstract fun <R : Any> flatMap(mapper: (T) -> Optional<R>): Optional<R>

    abstract fun <O : Any, R : Any> combineWith(other: Optional<O>, combiner: (T, O) -> R?): Optional<R>

    inline fun <reified R : Any> cast(): Optional<R> {
        return if (value is R)
            optionalOf(value as R)
        else
            Absent
    }

    open operator fun component1(): T? = value
}

object Absent : Optional<Nothing>() {

    override val value: Nothing? get() = null

    override val unsafeValue: Nothing get() = throw NoSuchElementException("Value is null")

    override val isPresent: Boolean get() = false

    override val isAbsent: Boolean get() = true

    override fun ifPresent(consumer: (Nothing) -> Unit) = this

    override fun ifAbsent(action: () -> Unit): Absent {
        action()
        return this
    }

    override fun filter(predicate: (Nothing) -> Boolean): Optional<Nothing> = this

    override fun <R : Any> map(mapper: (Nothing) -> R?): Optional<R> = this

    override fun <R : Any> flatMap(mapper: (Nothing) -> Optional<R>): Optional<R> = this

    override fun <O : Any, R : Any> combineWith(other: Optional<O>, combiner: (Nothing, O) -> R?): Optional<R> = this

    override fun toString(): String = "Absent"
}

data class Present<out T : Any>(override val value: T) : Optional<T>() {

    override val unsafeValue: T get() = value

    override val isPresent: Boolean get() = true

    override val isAbsent: Boolean get() = false

    override fun ifPresent(consumer: (T) -> Unit): Present<T> {
        consumer(value)
        return this
    }

    override fun ifAbsent(action: () -> Unit) = this

    override fun filter(predicate: (T) -> Boolean): Optional<T> {
        return if (predicate(value))
            this
        else
            Absent
    }

    override fun <R : Any> map(mapper: (T) -> R?): Optional<R> {
        val mapped = mapper(value)
        return optionalOf(mapped)
    }

    override fun <R : Any> flatMap(mapper: (T) -> Optional<R>): Optional<R> = mapper(value)

    override fun <O : Any, R : Any> combineWith(other: Optional<O>, combiner: (T, O) -> R?): Optional<R> {
        return other.map { otherValue ->
            combiner(value, otherValue)
        }
    }

    override fun toString(): String = "Present[$value]"
}
