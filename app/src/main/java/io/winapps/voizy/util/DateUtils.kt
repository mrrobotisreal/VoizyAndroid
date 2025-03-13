package io.winapps.voizy.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Duration
import java.time.Instant

class DateUtils {
}

@RequiresApi(Build.VERSION_CODES.O)
fun getTimeAgo(createdAt: String): String {
    val instant = Instant.parse(createdAt)
    val now = Instant.now()
    val duration = Duration.between(instant, now)
    val seconds = duration.seconds

    return when {
        seconds < 60 -> "Just now"
        seconds < 120 -> "1 min ago"
        seconds < 3600 -> "${seconds / 60} mins ago"
        seconds < 7200 -> "1 hour ago"
        seconds < 86400 -> "${seconds / 3600} hours ago"
        seconds < 172800 -> "Yesterday"
        seconds < 604800 -> "${seconds / 86400} days ago"
        seconds < 2419200 -> "${seconds / 604800} weeks ago"
        seconds < 29030400 -> "${seconds / 2419200} months ago"
        else -> "${seconds / 29030400} years ago"
    }
}