package se.gustavkarlsson.koptional

import kotlin.contracts.ExperimentalContracts

private val present: Present<Any> = Present("foobar")
private val optional: Optional<Any> = present

@Suppress("unused")
@ExperimentalContracts
fun `only needs to compile`() {
    if (optional.isPresent()) {
        TODO("takeAny(optional.value)")
    }

    if (optional.isAbsent()) {
        takeNothing(optional.value)
    }

    val a: String
    present.ifPresent {
        a = "foo"
        takeAny(a)
    }

    val b: String
    optional.ifPresent {
        b = "foobar"
        takeAny(b)
    }

    val c: String
    Absent.ifAbsent {
        c = "foo"
        takeAny(c)
    }

    val d: String
    optional.ifAbsent {
        d = "foobar"
        takeAny(d)
    }
}

@Suppress("UNUSED_PARAMETER")
private fun takeAny(any: Any) = Unit

@Suppress("UNUSED_PARAMETER")
private fun takeNothing(nothing: Nothing?) = Unit
