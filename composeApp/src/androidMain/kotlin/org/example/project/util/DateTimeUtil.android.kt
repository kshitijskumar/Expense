package org.example.project.util

actual fun DateTimeUtil.getCurrentTimeMillis(): Long = System.currentTimeMillis()
