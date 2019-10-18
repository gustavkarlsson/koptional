package se.gustavkarlsson.koptional

import org.junit.Test
import kotlin.contracts.ExperimentalContracts

@ExperimentalContracts
class ContractsTest {

    private val optional: Optional<Any> = Present("foobar")

    @Test
    fun isPresent() {
        if (optional.isPresent()) {
            TODO("takeAny(optional.value)")
        }
    }

    @Test
    fun isAbsent() {
        if (optional.isAbsent()) {
            takeNothing(optional.value)
        }
    }
}

private fun takeAny(any: Any) = Unit

private fun takeNothing(nothing: Nothing?) = Unit
