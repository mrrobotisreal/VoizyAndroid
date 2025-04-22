package io.winapps.voizy.ui.theme

import androidx.compose.ui.graphics.Color
import io.winapps.voizy.R

val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

data class ColorMap(
    val primaryColor: Color,
    val primaryAccent: Color,
    val secondaryColor: Color,
    val secondaryAccent: Color
)

fun getColorMap(
    primaryColorString: String,
    primaryAccentString: String,
    secondaryColorString: String,
    secondaryAccentString: String
): ColorMap {
    var primaryColorColor = VoizyPrimaryColor
    var primaryAccentColor = VoizyPrimaryAccent
    var secondaryColorColor = VoizySecondaryColor
    var secondaryAccentColor = VoizySecondaryAccent

    when(primaryColorString) {
        "yellow" -> {
            primaryColorColor = VoizyPrimaryColor
        }
        "oceanic-primary-color" -> {
            primaryColorColor = OceanicPrimaryColor
        }
        "royal-primary-color" -> {
            primaryColorColor = RoyalPrimaryColor
        }
        "sunset-primary-color" -> {
            primaryColorColor = SunsetPrimaryColor
        }
        else -> {
            primaryColorColor = VoizyPrimaryColor
        }
    }

    when(primaryAccentString) {
        "pale-yellow" -> {
            primaryAccentColor = VoizyPrimaryAccent
        }
        "oceanic-primary-accent" -> {
            primaryAccentColor = OceanicPrimaryAccent
        }
        "royal-primary-accent" -> {
            primaryAccentColor = RoyalPrimaryAccent
        }
        "sunset-primary-accent" -> {
            primaryAccentColor = SunsetPrimaryAccent
        }
        else -> {
            primaryAccentColor = VoizyPrimaryAccent
        }
    }

    when(secondaryColorString) {
        "magenta" -> {
            secondaryColorColor = VoizySecondaryColor
        }
        "oceanic-secondary-color" -> {
            secondaryColorColor = OceanicSecondaryColor
        }
        "royal-secondary-color" -> {
            secondaryColorColor = RoyalSecondaryColor
        }
        "sunset-secondary-color" -> {
            secondaryColorColor = SunsetSecondaryColor
        }
        else -> {
            secondaryColorColor = VoizySecondaryColor
        }
    }

    when(secondaryAccentString) {
        "pale-magenta" -> {
            secondaryAccentColor = VoizySecondaryAccent
        }
        "oceanic-secondary-accent" -> {
            secondaryAccentColor = OceanicSecondaryAccent
        }
        "royal-secondary-accent" -> {
            secondaryAccentColor = RoyalSecondaryAccent
        }
        "sunset-secondary-accent" -> {
            secondaryAccentColor = SunsetSecondaryAccent
        }
        else -> {
            secondaryAccentColor = VoizySecondaryAccent
        }
    }

    return ColorMap(
        primaryColor = primaryColorColor,
        primaryAccent = primaryAccentColor,
        secondaryColor = secondaryColorColor,
        secondaryAccent = secondaryAccentColor,
    )
}

data class ColorResourceMap(
    val primaryColor: Int = R.color.vibrant_yellow,
    val primaryAccent: Int = R.color.pale_yellow,
    val secondaryColor: Int = R.color.vibrant_magenta,
    val secondaryAccent: Int = R.color.pale_magenta
)

fun getColorResource(colorString: String): Int {
    when(colorString) {
        "yellow" -> {
            return R.color.vibrant_yellow
        }
        "pale-yellow" -> {
            return R.color.pale_yellow
        }
        "magenta" -> {
            return R.color.vibrant_magenta
        }
        "pale-magenta" -> {
            return R.color.pale_magenta
        }
        "oceanic-primary-color" -> {
            return R.color.oceanic_primary_color
        }
        "oceanic-primary-accent" -> {
            return R.color.oceanic_primary_accent
        }
        "oceanic-secondary-color" -> {
            return R.color.oceanic_secondary_color
        }
        "oceanic-secondary-accent" -> {
            return R.color.oceanic_secondary_accent
        }
        "royal-primary-color" -> {
            return R.color.royal_primary_color
        }
        "royal-primary-accent" -> {
            return R.color.royal_primary_accent
        }
        "royal-secondary-color" -> {
            return R.color.royal_secondary_color
        }
        "royal-secondary-accent" -> {
            return R.color.royal_secondary_accent
        }
        "sunset-primary-color" -> {
            return R.color.sunset_primary_color
        }
        "sunset-primary-accent" -> {
            return R.color.sunset_primary_accent
        }
        "sunset-secondary-color" -> {
            return R.color.sunset_secondary_color
        }
        "sunset-secondary-accent" -> {
            return R.color.sunset_secondary_accent
        }
        else -> {
            return R.color.vibrant_yellow
        }
    }
}

fun getColorResourceMap(
    primaryColorString: String = "yellow",
    primaryAccentString: String = "pale-yellow",
    secondaryColorString: String = "magenta",
    secondaryAccentString: String = "pale-magenta"
): ColorResourceMap {
    var primaryColorResource = R.color.vibrant_yellow
    var primaryAccentResource = R.color.pale_yellow
    var secondaryColorResource = R.color.vibrant_magenta
    var secondaryAccentResource = R.color.pale_magenta

    when(primaryColorString) {
        "yellow" -> {
            primaryColorResource = R.color.vibrant_yellow
        }
        "oceanic-primary-color" -> {
            primaryColorResource = R.color.oceanic_primary_color
        }
        "royal-primary-color" -> {
            primaryColorResource = R.color.royal_primary_color
        }
        "sunset-primary-color" -> {
            primaryColorResource = R.color.sunset_primary_color
        }
        else -> {
            primaryColorResource = R.color.vibrant_yellow
        }
    }

    when(primaryAccentString) {
        "pale-yellow" -> {
            primaryAccentResource = R.color.pale_yellow
        }
        "oceanic-primary-accent" -> {
            primaryAccentResource = R.color.oceanic_primary_accent
        }
        "royal-primary-accent" -> {
            primaryAccentResource = R.color.royal_primary_accent
        }
        "sunset-primary-accent" -> {
            primaryAccentResource = R.color.sunset_primary_accent
        }
        else -> {
            primaryAccentResource = R.color.pale_yellow
        }
    }

    when(secondaryAccentString) {
        "magenta" -> {
            secondaryColorResource = R.color.vibrant_magenta
        }
        "oceanic-secondary-color" -> {
            secondaryColorResource = R.color.oceanic_secondary_color
        }
        "royal-secondary-color" -> {
            secondaryColorResource = R.color.royal_secondary_color
        }
        "sunset-secondary-color" -> {
            secondaryColorResource = R.color.sunset_secondary_color
        }
        else -> {
            secondaryColorResource = R.color.vibrant_magenta
        }
    }

    when(secondaryAccentString) {
        "pale-magenta" -> {
            secondaryAccentResource = R.color.pale_magenta
        }
        "oceanic-secondary-accent" -> {
            secondaryAccentResource = R.color.oceanic_secondary_accent
        }
        "royal-secondary-accent" -> {
            secondaryAccentResource = R.color.royal_secondary_accent
        }
        "sunset-secondary-accent" -> {
            secondaryAccentResource = R.color.sunset_secondary_accent
        }
        else -> {
            secondaryAccentResource = R.color.pale_magenta
        }
    }

    return ColorResourceMap(
        primaryColor = primaryColorResource,
        primaryAccent = primaryAccentResource,
        secondaryColor = secondaryColorResource,
        secondaryAccent = secondaryAccentResource
    )
}

// APP THEMES below
// Voizy Theme (default)
val VoizyPrimaryColor = Color(0xFFF9D841)
val VoizyPrimaryAccent = Color(0xFFFDF4C9)
val VoizySecondaryColor = Color(0xFFF10E91)
val VoizySecondaryAccent = Color(0xFFFFD5ED)

// Oceanic
val OceanicPrimaryColor = Color(0xFF00796B)
val OceanicPrimaryAccent = Color(0xFF4DB6AC)
val OceanicSecondaryColor = Color(0xFFE64A19)
val OceanicSecondaryAccent = Color(0xFFFFAB91)

// Royal
val RoyalPrimaryColor = Color(0xFF303F9F)
val RoyalPrimaryAccent = Color(0xFF7986CB)
val RoyalSecondaryColor = Color(0xFFFFC107)
val RoyalSecondaryAccent = Color(0xFFFFECB3)

// Sunset
val SunsetPrimaryColor = Color(0xFFF57C00)
val SunsetPrimaryAccent = Color(0xFFFFB74D)
val SunsetSecondaryColor = Color(0xFF7B1FA2)
val SunsetSecondaryAccent = Color(0xFFCE93D8)

// Forest

// Cool Steel

// Playful