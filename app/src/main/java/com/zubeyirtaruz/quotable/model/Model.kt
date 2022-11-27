package com.zubeyirtaruz.quotable.model

import com.zubeyirtaruz.quotable.R
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Quote(
    @SerialName("text")
    val quote: String?,
    var author: String?,
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


