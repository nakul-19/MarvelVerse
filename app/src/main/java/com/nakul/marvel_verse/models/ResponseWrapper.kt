package com.nakul.marvel_verse.models

data class ResponseWrapper<T: Any>(
    val data: Data<T>
)