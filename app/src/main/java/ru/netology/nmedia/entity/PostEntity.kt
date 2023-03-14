package ru.netology.nmedia.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import ru.netology.nmedia.dto.Attachment
import ru.netology.nmedia.dto.Post

@Entity
data class PostEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val author: String,
    val authorAvatar: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int = 0,
    val attachment: AttachmentEntity?
) {
    fun toDto() = Post(
        id, author, authorAvatar, content, published, likedByMe, likes, attachment?.toDto()
    )

    companion object {
        fun fromDto(dto: Post) =
            PostEntity(
                dto.id,
                dto.author,
                dto.authorAvatar,
                dto.content,
                dto.published,
                dto.likedByMe,
                dto.likes,
                AttachmentEntity.fromDto(dto.attachment)
            )
    }
}

data class AttachmentEntity(
    val url: String,
    val description: String,
    val type: String
) {
    fun toDto() = Attachment(url, description, type)

    companion object {
        fun fromDto(dto: Attachment?) =
            dto?.let { AttachmentEntity(it.url, it.description, it.type) }
    }
}

class AttachmentTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromJson(json: String?): AttachmentEntity? {
        return gson.fromJson(json, AttachmentEntity::class.java)
    }

    @TypeConverter
    fun toJson(attachment: AttachmentEntity?): String? {
        return gson.toJson(attachment)
    }
}
