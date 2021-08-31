package com.nakul.marvel_verse.utils

import com.nakul.marvel_verse.models.Thumbnail

const val NO_FILTER = 0
const val THIS_WEEK = 1
const val NEXT_WEEK = 2
const val LAST_WEEK = 3
const val THIS_MONTH = 4

const val page_size = 20
const val resource_type="/portrait_fantastic"

fun Thumbnail.toUrl(): String {
    return this.path+ resource_type + this.extension
}