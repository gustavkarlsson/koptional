package se.gustavkarlsson.koptional

import assertk.assert
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEqualTo
import assertk.assertions.isTrue
import org.junit.Test

class CreationTest {

    @Test
    fun optionalOfValue() {
        val result = optionalOf("test")
        assert(result).isEqualTo(Present("test"))
    }

    @Test
    fun optionalOfNull() {
        val result = optionalOf(null)
        assert(result).isEqualTo(Absent)
    }

    @Test
    fun toOptionalValue() {
        val result = "test".toOptional()
        assert(result).isEqualTo(Present("test"))
    }

    @Test
    fun toOptionalNull() {
        val result = (null as String?).toOptional()
        assert(result).isEqualTo(Absent)
    }
}
