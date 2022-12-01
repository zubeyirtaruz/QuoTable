package com.zubeyirtaruz.quotable.util

fun editMessage(string: String): String{
    return string.substring(string.indexOf("message: ") + 9,string.length)
}