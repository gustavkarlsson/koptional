package se.gustavkarlsson.koptional

import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

sealed class Optional<out T : Any> {

    abstract val value: T?

    abstract val valueUnsafe: T

    @Deprecated(message = "Property deprecated in favor of function", replaceWith = ReplaceWith("isPresent()"))
    abstract val isPresent: Boolean

    @Deprecated(message = "Property deprecated in favor of function", replaceWith = ReplaceWith("isAbsent()"))
    abstract val isAbsent: Boolean

    abstract fun filter(predicate: (T) -> Boolean): Optional<T>

    abstract fun <R : Any> map(mapper: (T) -> R?): Optional<R>

    abstract fun <R : Any> flatMap(mapper: (T) -> Optional<R>): Optional<R>

    abstract fun <O : Any, R : Any> combineWith(other: Optional<O>, combiner: (T, O) -> R?): Optional<R>

    inline fun <reified R : Any> cast(): Optional<R> =
        if (value is R)
            optionalOf(value as R)
        else
            Absent

    open operator fun component1(): T? = value
}

object Absent : Optional<Nothing>() {

    override val value: Nothing? get() = null

    override val valueUnsafe: Nothing get() = throw NoSuchElementException("Value is null")

    override val isPresent: Boolean get() = false

    override val isAbsent: Boolean get() = true

    override fun filter(predicate: (Nothing) -> Boolean): Optional<Nothing> = this

    override fun <R : Any> map(mapper: (Nothing) -> R?): Optional<R> = this

    override fun <R : Any> flatMap(mapper: (Nothing) -> Optional<R>): Optional<R> = this

    override fun <O : Any, R : Any> combineWith(other: Optional<O>, combiner: (Nothing, O) -> R?): Optional<R> = this

    override fun toString(): String = "Absent"
}

data class Present<out T : Any>(override val value: T) : Optional<T>() {

    override val valueUnsafe: T get() = value

    override val isPresent: Boolean get() = true

    override val isAbsent: Boolean get() = false

    override fun filter(predicate: (T) -> Boolean): Optional<T> =
        if (predicate(value))
            this
        else
            Absent

    override fun <R : Any> map(mapper: (T) -> R?): Optional<R> = optionalOf(mapper(value))

    override fun <R : Any> flatMap(mapper: (T) -> Optional<R>): Optional<R> = mapper(value)

    override fun <O : Any, R : Any> combineWith(other: Optional<O>, combiner: (T, O) -> R?): Optional<R> =
        other.map { otherValue ->
            combiner(value, otherValue)
        }

    override fun toString(): String = "Present[$value]"
}

fun <T : Any> Optional<T>.valueOr(other: () -> T): T = value ?: other()

@ExperimentalContracts
fun <T: Any> Optional<T>.isPresent(): Boolean {
    contract {
        returns(true) implies (this@isPresent is Present<T>)
        returns(false) implies (this@isPresent is Absent)
    }
    return this is Present
}

@ExperimentalContracts
fun <T: Any> Optional<T>.isAbsent(): Boolean {
    contract {
        returns(true) implies (this@isAbsent is Absent)
        returns(false) implies (this@isAbsent is Present<T>)
    }
    return this is Absent
}

@ExperimentalContracts
inline fun <T: Any> Optional<T>.ifPresent(consumer: (T) -> Unit): Optional<T> {
    contract {
        callsInPlace(consumer, InvocationKind.AT_MOST_ONCE)
    }
    return this.also { if (this is Present) consumer(value) }
}

@ExperimentalContracts
inline fun <T: Any> Present<T>.ifPresent(consumer: (T) -> Unit): Present<T> {
    contract {
        callsInPlace(consumer, InvocationKind.EXACTLY_ONCE)
    }
    return this.also { consumer(value) }
}

@ExperimentalContracts
inline fun <T: Any> Optional<T>.ifAbsent(action: () -> Unit): Optional<T> {
    contract {
        callsInPlace(action, InvocationKind.AT_MOST_ONCE)
    }
    return this.also { if (this is Absent) action() }
}

@ExperimentalContracts
inline fun Absent.ifAbsent(action: () -> Unit): Absent {
    contract {
        callsInPlace(action, InvocationKind.EXACTLY_ONCE)
    }
    return this.also { action() }
}
