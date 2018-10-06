package se.gustavkarlsson.koptional

import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEqualTo
import assertk.assertions.isTrue
import org.junit.Test

class OptionalTest {

    private val inner = "foo"
    private val other = "bar"

    @Test
    fun equalsEqual() {
        assert(Present(inner)).isEqualTo(Present(inner))
    }

    @Test
    fun equalsOther() {
        assert(Present(inner)).isNotEqualTo(Present(other))
    }

    @Test
    fun equalsAbsent() {
        assert(Present(inner)).isNotEqualTo(Absent)
    }

    @Test
    fun hashCodeEqual() {
        assert(Present(inner).hashCode()).isEqualTo(Present(inner).hashCode())
    }

    @Test
    fun hashCodeOther() {
        assert(Present(inner).hashCode()).isNotEqualTo(Present(other).hashCode())
    }

    @Test
    fun hashCodeAbsent() {
        assert(Present(inner).hashCode()).isNotEqualTo(Absent.hashCode())
    }
}
