package com.zubeyirtaruz.quotable.model

import com.huawei.agconnect.cloud.database.CloudDBZoneObject
import com.huawei.agconnect.cloud.database.annotations.PrimaryKeys
import com.zubeyirtaruz.quotable.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Quote(
    @SerialName("q")
    val quote: String?,
    @SerialName("a")
    var author: String?,
    @SerialName("i")
    var imageUrl: String?,
    val book: String? = null

)

data class Template(
    val layoutId: Int,
    val vip: Boolean,
    val isSelected: Boolean = false
){
    val strokeWidth = when {
        isSelected -> R.dimen.card_stroke_width
        else -> R.dimen.zero
    }
}

@Serializable
data class Author(
    @SerialName("a")
    val name: String?,
    @SerialName("i")
    var imageUrl: String?,
    @SerialName("t")
    val nameTitle: String?,
    val isSelected: Boolean = false
)

@Serializable
data class Category(
    val categoryName: String?,
    val isSelected: Boolean = false
)



@PrimaryKeys("quote")
data class FavoriteQuote(
    var userUid: String,
    var quote: String,
    var author: String
) : CloudDBZoneObject(FavoriteQuote::class.java)


