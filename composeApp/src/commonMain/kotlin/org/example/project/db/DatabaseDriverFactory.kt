package org.example.project.db

import app.cash.sqldelight.db.SqlDriver

interface DatabaseDriverFactory {
    fun createDriver(): SqlDriver
    
    companion object {
        const val DB_NAME = "expense.db"
    }
}
