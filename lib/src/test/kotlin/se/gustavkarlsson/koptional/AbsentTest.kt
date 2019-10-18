package se.gustavkarlsson.koptional

import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNull
import assertk.assertions.isTrue
import org.junit.Test
import kotlin.contracts.ExperimentalContracts

@ExperimentalContracts
class AbsentTest {

    private val other = "bar"
    private val absent = Absent as Optional<String>

    @Test
    fun value() {
        val result = absent.value
        assert(result).isNull()
    }

    @Test(expected = NoSuchElementException::class)
    fun valueUnsafe() {
        absent.valueUnsafe
    }

    @Test
    fun isPresent() {
        val result = absent.isPresent
        assert(result).isFalse()
    }

    @Test
    fun isAbsent() {
        val result = absent.isAbsent
        assert(result).isTrue()
    }

    @Test
    fun ifPresent() {
        var wasRun = false
        absent.ifPresent { wasRun = true }
        assert(wasRun).isFalse()
    }

    @Test
    fun ifAbsent() {
        var wasRun = false
        absent.ifAbsent { wasRun = true }
        assert(wasRun).isTrue()
    }

    @Test
    fun filterMatches() {
        val result = absent.filter { true }
        assert(result).isEqualTo(Absent)
    }

    @Test
    fun filterDoesNotMatch() {
        val result = absent.filter { false }
        assert(result).isEqualTo(Absent)
    }

    @Test
    fun map() {
        val result = absent.map { it + other }
        assert(result).isEqualTo(Absent)
    }

    @Test
    fun mapNull() {
        val result = absent.map { null }
        assert(result).isEqualTo(Absent)
    }

    @Test
    fun flatMapPresent() {
        val result = absent.flatMap { Present(it + other) }
        assert(result).isEqualTo(Absent)
    }

    @Test
    fun flatMapAbsent() {
        val result = absent.flatMap { Absent }
        assert(result).isEqualTo(Absent)
    }

    @Test
    fun combineWithPresent() {
        val result = absent.combineWith(Present(other), String::plus)
        assert(result).isEqualTo(Absent)
    }

    @Test
    fun combineWithPresentNull() {
        val result = absent.combineWith(Present(other)) { _, _ -> null }
        assert(result).isEqualTo(Absent)
    }

    @Test
    fun combineWithAbsent() {
        val result = absent.combineWith(Absent, String::plus)
        assert(result).isEqualTo(Absent)
    }

    @Test
    fun combineWithAbsentNull() {
        val result = absent.combineWith(Absent) { _, _ -> null }
        assert(result).isEqualTo(Absent)
    }

    @Test
    fun toStringTest() {
        absent.toString()
    }

    @Test
    fun castValid() {
        val result = absent.cast<CharSequence>()
        assert(result).isEqualTo(Absent)
    }

    @Test
    fun castInvalid() {
        val result = absent.cast<List<String>>()
        assert(result).isEqualTo(Absent)
    }

    @Test
    fun destructuring() {
        val (result) = absent
        assert(result).isNull()
    }

    @Test
    fun valueOr() {
        val result = absent.valueOr { other }
        assert(result).isEqualTo(other)
    }
}
