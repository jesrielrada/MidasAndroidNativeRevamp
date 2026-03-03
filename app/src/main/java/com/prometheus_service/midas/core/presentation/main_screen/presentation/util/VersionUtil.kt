package com.prometheus_service.midas.core.presentation.main_screen.presentation.util

fun String.isOlderThan(otherVersion: String): Boolean {
    val thisParts = this.split('.').map { it.toIntOrNull() ?: 0 }
    val otherParts = otherVersion.split(".").map { it.toIntOrNull() ?: 0 }
    val maxLength = maxOf(thisParts.size, otherParts.size)

    for (i in 0 until maxLength) {
        val thisPart = thisParts.getOrElse(i) { 0 }
        val otherPart = otherParts.getOrElse(i) { 0 }

        if (thisPart < otherPart) return true
        if (thisPart > otherPart) return false
    }

    return false
}