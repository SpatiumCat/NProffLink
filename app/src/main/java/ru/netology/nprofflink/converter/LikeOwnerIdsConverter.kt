package ru.netology.nprofflink.converter

import androidx.room.TypeConverter
import ru.netology.nprofflink.dto.UserPreview
import java.util.stream.Collectors

class LikeOwnerIdsConverter {

    @TypeConverter
    fun fromLikeOwnerIds (list: List<Long>?): String? {
        return list?.joinToString()
    }

    @TypeConverter
    fun toLikeOwnerIds (string: String?): List<Long>? {
        if(string.isNullOrBlank()) return null
        return string.split(",").map { it.trim().toLong() }
    }

    @TypeConverter
    fun fromListUserPreview (list: List<UserPreview>): String {
       return  list.joinToString(";") { it.name + "," + it.avatar }
    }
    @TypeConverter
    fun toListUserPreview (string: String): List<UserPreview> {
        val listPreview = mutableListOf<UserPreview>()
        string.split(";").forEach {
            val split = it.split(",")
            listPreview.add(UserPreview(name = split[0], avatar = split[1]))
        }
        return listPreview
    }
}