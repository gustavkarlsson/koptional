[![Build Status](https://travis-ci.com/gustavkarlsson/koptional.svg?branch=master)](https://travis-ci.com/gustavkarlsson/koptional)
[![codecov](https://codecov.io/gh/gustavkarlsson/koptional/branch/master/graph/badge.svg)](https://codecov.io/gh/gustavkarlsson/koptional)
[![Version](https://jitpack.io/v/gustavkarlsson/koptional.svg)](https://jitpack.io/#gustavkarlsson/koptional)
[![MIT licensed](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/gustavkarlsson/koptional/blob/master/LICENSE.md)

# Koptional

Koptional is a Kotlin-optimized [option type](https://en.wikipedia.org/wiki/Option_type).

## FAQ

**Isn't this pointless? Kotlin already has this built into the type system!**

In many cases yes, but some libraries (most notably [RxJava](https://github.com/ReactiveX/RxJava))
does not allow for null values.

**What's wrong with all the other option type libraries out there?**

They all "get the job done", but no one got it "just right" for me.

**Please elaborate!**

I wanted something that:

* Makes full use of the kotlin type system
* Has functional constructs such as `map` and `filter`
* Uses the same terminology as [java.util.Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html)
(optional, absent, present)

## Usage

Create an Optional:

```kotlin
                               // Declared type      Actual type
optionalOf("something")        // Optional<String>   Present
optionalOf(null)               // Optional<Nothing>  Absent

"something".toOptional()       // Optional<String>   Present
null.toOptional()              // Optional<Nothing>  Absent

Present("something")           // Present<String>    Present
Absent                         // Absent             Absent
```

Get the wrapped value:

```kotlin
val myOptional = optionalOf("something")
myOptional.value               // String?
myOptional.valueUnsafe         // String or throws
myOptional.valueOr { "other" } // String
val (value) = myOptional       // String?
```

Inspect contents

```kotlin
myOptional.isPresent
myOptional.isAbsent
myOptional.ifPresent { println(it) }
myOptional.ifAbsent { println("nothing here") }
```

Functional constructs

```kotlin
val myOptional = optionalOf("something")
myOptional.filter { it.length > 5 } // Absent if condition fails
myOptional.map { "I like $it" } // Also allows for null return values
myOptional.flatMap { returnsOptional(it) } // use wrapped value to produce new Optional
myOptional.combineWith(otherOptional) { my, other -> my + optional } // Also allows for null return values
myOptional.cast<CharSequence>() // Absent if casting not possible
```

## Download

Koptional is hosted on JitPack. Here's how you include it in your gradle project:

**Step 1.** Add the JitPack repository to your build file:

```groovy
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

**Step 2.** Add the dependency:

```groovy
dependencies {
    implementation 'com.github.gustavkarlsson:koptional:<latest_version>'
}
```
