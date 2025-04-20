package io.winapps.voizy.util

fun GetDisplayName(username: String, preferredName: String, firstName: String?, lastName: String?): String {
    val displayName = if (!firstName.isNullOrBlank() && !lastName.isNullOrBlank()) {
        "$firstName $lastName ($username)"
    } else if (!lastName.isNullOrBlank()) {
        "$preferredName $lastName ($username)"
    } else {
        "$preferredName ($username)"
    }
    return displayName
}