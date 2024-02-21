package ru.netology.nprofflink.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import ru.netology.nprofflink.converter.LikeOwnerIdsConverter
import ru.netology.nprofflink.converter.MentionsIdsConverter
import ru.netology.nprofflink.dto.Attachment
import ru.netology.nprofflink.dto.Coordinates
import ru.netology.nprofflink.dto.Post
import ru.netology.nprofflink.dto.User
import ru.netology.nprofflink.dto.UserPreview
import ru.netology.nprofflink.dto.Users

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Long,
    val authorId: Long,
    val author: String,
    val authorAvatar: String?,
    val authorJob: String?,
    val content: String,
    val published: String,

    @Embedded
    val coords: Coordinates?,
    val link: String?,
    @TypeConverters(LikeOwnerIdsConverter::class)
    val likeOwnerIds: List<Long>?,
    @TypeConverters(LikeOwnerIdsConverter::class)
    val mentionsIds: List<Long>?,
    val mentionedMe: Boolean,
    val likedByMe: Boolean,

    @Embedded
    val attachment: Attachment?,
    val ownedByMe: Boolean,
//    @Embedded
//    @TypeConverters(LikeOwnerIdsConverter::class)
//   // val users: List<UserPreview>,
) {
    fun toDto() = Post(
        id = id,
        authorId = authorId,
        author = author,
        authorAvatar = authorAvatar,
        authorJob = authorJob,
        content = content,
        published = published,
        coords = coords,
        link = link,
        likeOwnerIds = likeOwnerIds ?: emptyList(),
        mentionsIds = mentionsIds ?: emptyList(),
        mentionedMe = mentionedMe,
        likedByMe = likedByMe,
        attachment = attachment,
        ownedByMe = ownedByMe,
//        users = users,
    )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                id = dto.id,
                authorId = dto.authorId,
                author = dto.author,
                authorAvatar = dto.authorAvatar,
                authorJob = dto.authorJob,
                content = dto.content,
                published = dto.published,
                coords = dto.coords,
                link = dto.link,
                likeOwnerIds = dto.likeOwnerIds,
                mentionsIds = dto.mentionsIds,
                mentionedMe = dto.mentionedMe,
                likedByMe = dto.likedByMe,
                attachment = dto.attachment,
                ownedByMe = dto.ownedByMe,
//                users = dto.users
            )
    }
}

fun List<PostEntity>.toDto(): List<Post> = map(PostEntity::toDto)
fun List<Post>.toEntity(): List<PostEntity> = map { PostEntity.fromDto(it) }