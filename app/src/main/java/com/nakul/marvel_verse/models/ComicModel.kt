package com.nakul.marvel_verse.models

data class ComicModel(
    val dates: ArrayList<Date>,
    val thumbnail: Thumbnail,
    val title: String
)

data class Date(
    val date: String,
    val type: String
)
