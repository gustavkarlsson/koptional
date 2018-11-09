package se.gustavkarlsson.koptional

fun <T : Any> absent(): Optional<T> = Absent

fun <T : Any> optionalOf(value: T?): Optional<T> = if (value == null) Absent else Present(value)

fun <T : Any> T?.toOptional(): Optional<T> = optionalOf(this)
