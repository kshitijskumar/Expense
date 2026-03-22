package org.example.project.util

import platform.Foundation.NSDate
import platform.Foundation.timeIntervalSince1970

actual object DateTimeUtil {
    actual fun getCurrentTimeMillis(): Long {
        return (NSDate().timeIntervalSince1970 * 1000).toLong()
    }
}