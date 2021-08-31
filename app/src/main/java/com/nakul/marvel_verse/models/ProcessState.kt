package com.nakul.marvel_verse.models

sealed class ProcessState<T: Any> {
    class Success<T: Any>(val res: T): ProcessState<T>()
    class Failure<T: Any>(val message: String): ProcessState<T>()
    class Loading<T: Any>: ProcessState<T>()
}
