package com.ssg.ic.sp

fun String.toCamel(): String {
    return lowercase().replace("_[a-z]".toRegex()) { it.value.last().uppercase() }
}