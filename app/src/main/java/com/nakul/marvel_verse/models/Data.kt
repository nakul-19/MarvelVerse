package com.nakul.marvel_verse.models

data class Data<T:Any>(
    val count: Int,
    val offset: Int,
    val results: ArrayList<T>
)