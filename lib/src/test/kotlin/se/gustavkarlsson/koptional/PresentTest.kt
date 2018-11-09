package se.gustavkarlsson.koptional

import assertk.assert
import assertk.assertions.contains
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import assertk.fail
import org.junit.Test

class PresentTest {

    private val inner = "foo"
    private val other = "bar"
    private val present = Present(inner)

    @Test
    fun value() {
        val result = present.value
        assert(result).isEqualTo(inner)
    }

    @Test
    fun valueUnsafe() {
        val result = present.valueUnsafe
        assert(result).isEqualTo(inner)
    }

    @Test
    fun isPresent() {
        val result = present.isPresent
        assert(result).isTrue()
    }

    @Test
    fun isAbsent() {
        val result = present.isAbsent
        assert(result).isFalse()
    }

    @Test
    fun ifPresent() {
        var wasRun = false
        present.ifPresent { wasRun = true }
        assert(wasRun).isTrue()
    }

    @Test
    fun ifAbsent() {
        var wasRun = false
        present.ifAbsent { wasRun = true }
        assert(wasRun).isFalse()
    }

    @Test
    fun filterMatches() {
        val result = present.filter { it.length > 2 }
        assert(result).isEqualTo(present)
    }

    @Test
    fun filterDoesNotMatch() {
        val result = present.filter { it.length > 4 }
        assert(result).isEqualTo(Absent)
    }

    @Test
    fun map() {
        val result = present.map { it + other }
        assert(result).isEqualTo(Present(inner + other))
    }

    @Test
    fun mapNull() {
        val result = present.map { null }
        assert(result).isEqualTo(Absent)
    }

    @Test
    fun flatMapPresent() {
        val result = present.flatMap { Present(it + other) }
        assert(result).isEqualTo(Present(inner + other))
    }

    @Test
    fun flatMapAbsent() {
        val result = present.flatMap { Absent }
        assert(result).isEqualTo(Absent)
    }

    @Test
    fun combineWithPresent() {
        val result = present.combineWith(Present(other), String::plus)
        assert(result).isEqualTo(Present(inner + other))
    }

    @Test
    fun combineWithPresentNull() {
        val result = present.combineWith(Present(other)) { _, _ -> null }
        assert(result).isEqualTo(Absent)
    }

    @Test
    fun combineWithAbsent() {
        val result = present.combineWith(Absent, String::plus)
        assert(result).isEqualTo(Absent)
    }

    @Test
    fun combineWithAbsentNull() {
        val result = present.combineWith(Absent) { _, _ -> null }
        assert(result).isEqualTo(Absent)
    }

    @Test
    fun toStringTest() {
        val result = present.toString()
        assert(result).contains(inner)
    }

    @Test
    fun castValid() {
        val result = present.cast<CharSequence>()
        assert(result).isEqualTo(present)
    }

    @Test
    fun castInvalid() {
        val result = present.cast<List<String>>()
        assert(result).isEqualTo(Absent)
    }

    @Test
    fun destructuring() {
        val (result) = present
        assert(result).isEqualTo(inner)
    }

    @Test
    fun valueOr() {
        val result = present.valueOr { fail("This block should never run") }
        assert(result).isEqualTo(inner)
    }
}
