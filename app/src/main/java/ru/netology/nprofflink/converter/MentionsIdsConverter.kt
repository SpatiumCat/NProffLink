package ru.netology.nprofflink.converter

import androidx.room.TypeConverter

class MentionsIdsConverter {

    @TypeConverter
    fun fromMentionsIds (list: List<Int>): String {
        return list.joinToString()
    }

    @TypeConverter
    fun toMentionsIds (string: String): List<Int> {
        return string.split(",").map { it.trim().toInt() }
    }
}