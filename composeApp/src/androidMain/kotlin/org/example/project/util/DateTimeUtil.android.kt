package org.example.project.util


actual object DateTimeUtil {
    actual fun getCurrentTimeMillis(): Long = System.currentTimeMillis()
}