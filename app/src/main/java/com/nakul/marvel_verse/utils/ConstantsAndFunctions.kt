package com.nakul.marvel_verse.utils

import com.nakul.marvel_verse.models.ComicModel
import com.nakul.marvel_verse.models.Thumbnail
import java.math.BigInteger
import java.security.MessageDigest
import java.time.DayOfWeek
import java.time.LocalDate

const val NO_FILTER = 0
const val THIS_WEEK = 1
const val NEXT_WEEK = 2
const val LAST_WEEK = 3
const val THIS_MONTH = 4

const val appTitle = "<span style=\"color:rgb(200,0,0)\">MARVEL</span><span style=\"color:white\">VERSE</span>"
const val base_url = "https://gateway.marvel.com/v1/public/"
const val characters = "characters"
const val comics = "comics"
const val publicKey = "apikey"
const val md5hash = "hash"
const val timestamp = "ts"
const val resultLimit = "limit"
const val resultOffset = "offset"
const val order_by = "orderBy"
const val searchedName = "nameStartsWith"
const val character_page_size = 10
const val comic_page_size = 40
const val comic_offset = 90
const val comic_order_by = "-onsaleDate"
const val resource_type = "/portrait_fantastic"

fun Thumbnail.toUrl(): String {
    return this.path + resource_type + "." + this.extension
}

fun md5Hash(input: String): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
}

fun getTs() = (System.currentTimeMillis() / 1000).toString()


fun ComicModel.getDate(): String {
    for (i in this.dates)
        if (i.type=="onsaleDate")
            return i.date
    return ""
}

fun inThisWeek(str: String): Boolean {

    val today: LocalDate = LocalDate.now()

    var sunday: LocalDate = today
    while (sunday.dayOfWeek !== DayOfWeek.SUNDAY) {
        sunday = sunday.minusDays(1)
    }

    var saturday: LocalDate = today
    while (saturday.dayOfWeek !== DayOfWeek.SATURDAY) {
        saturday = saturday.plusDays(1)
    }

    return str>=sunday.toString() && str<=saturday.toString()
}

fun inLastWeek(str: String): Boolean {

    val today: LocalDate = LocalDate.now()

    var sunday: LocalDate = today.minusDays(7)
    while (sunday.dayOfWeek !== DayOfWeek.SUNDAY) {
        sunday = sunday.minusDays(1)
    }

    var saturday: LocalDate = today
    while (saturday.dayOfWeek !== DayOfWeek.SATURDAY) {
        saturday = saturday.plusDays(1)
    }

    return str>=sunday.toString() && str<=saturday.toString()
}

fun inNextWeek(str: String): Boolean {

    val today: LocalDate = LocalDate.now()

    var sunday: LocalDate = today
    while (sunday.dayOfWeek !== DayOfWeek.SUNDAY) {
        sunday = sunday.plusDays(1)
    }

    var saturday: LocalDate = sunday
    while (saturday.dayOfWeek !== DayOfWeek.SATURDAY) {
        saturday = saturday.plusDays(1)
    }

    return str>=sunday.toString() && str<=saturday.toString()
}

fun inThisMonth(str: String): Boolean {
    val today = LocalDate.now().toString()
    return str[5]==today[5] && str[6]==today[6]
}