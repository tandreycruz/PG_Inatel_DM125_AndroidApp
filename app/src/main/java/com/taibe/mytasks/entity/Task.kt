package com.taibe.mytasks.entity

import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class Task(
    val id: Long? = null,
    val title: String,
    val description: String? = null,
    val date: LocalDate? = null,
    val time: LocalTime? = null,
    val completed: Boolean = false
) : Serializable {

    fun formatDateNumeric(): String {
        return date?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) ?: ""
    }

    fun formatDateTextual(): String {
        val locale = Locale.forLanguageTag("pt-BR")

        val formatter = DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy", locale)

        return date?.format(formatter)?.replaceFirstChar { it.uppercase() } ?: ""
    }

    fun formatDate(preference: String): String {
        return when (preference) {
            "textual" -> formatDateTextual()
            else -> formatDateNumeric()
        }
    }

    fun formatDate(): String {
        return formatDateNumeric()
    }

    fun formatTime(): String {
        return time?.format(DateTimeFormatter.ofPattern("HH:mm")) ?: ""
    }

    fun formatDateTime(preference: String): String {
        val dateStr = formatDate(preference)
        val timeStr = formatTime()

        return when {
            date != null && time != null -> "$dateStr $timeStr"
            date != null -> dateStr
            time != null -> timeStr
            else -> "-"
        }
    }
}
