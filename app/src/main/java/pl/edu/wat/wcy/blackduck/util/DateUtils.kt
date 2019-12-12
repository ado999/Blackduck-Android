package pl.edu.wat.wcy.blackduck.util

import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class DateUtils {

    companion object {
        fun getDateDiff(creationDate: Date): String {
            val diff = abs(Date().time - creationDate.time)
            val days = TimeUnit.MILLISECONDS.toDays(diff)
            val weeks = days / 7L
            val hours = TimeUnit.MILLISECONDS.toHours(diff)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)

            return when {
                weeks > 0 -> "$weeks tyg."
                days > 0 -> "$days d."
                hours > 0 -> "$hours godz."
                minutes > 0 -> "$minutes min."
                else -> "Przed chwilą"
            }
        }
    }
}