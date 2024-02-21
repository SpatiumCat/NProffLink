package ru.netology.nprofflink.dto

import ru.netology.nprofflink.enums.EventType

data class Event(
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: Int,
    val authorJob: String,
    val content: String,
    val datetime: String,
    val published: String,
    val coords: Coordinates,
    val type: EventType,
    val likeOwnerIds: List<Long>,
    val likedByMe: Boolean,
    val speakerIds: List<Long>,
    val participantsIds: List<Long>,
    val participatedByMe: Boolean,
    val attachment: Attachment,
    val link: String,
    val ownedByMe: Boolean,
    val users: User
    )
