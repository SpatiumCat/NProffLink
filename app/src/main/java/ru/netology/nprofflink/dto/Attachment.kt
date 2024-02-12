package ru.netology.nprofflink.dto

import ru.netology.nprofflink.enums.AttachmentType

data class Attachment(
    val url: String,
    val type: AttachmentType
)
