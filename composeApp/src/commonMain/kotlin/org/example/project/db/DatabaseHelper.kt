package org.example.project.db

import app.cash.sqldelight.db.SqlDriver

/**
 * Database helper for initializing and accessing the expense tracker database.
 * 
 * Usage:
 * ```
 * // Android
 * val driverFactory = AndroidDatabaseDriverFactory(context)
 * val database = DatabaseHelper(driverFactory)
 * 
 * // iOS
 * val driverFactory = IosDatabaseDriverFactory()
 * val database = DatabaseHelper(driverFactory)
 * ```
 */
class DatabaseHelper(
    private val driverFactory: DatabaseDriverFactory
) {
    private val driver: SqlDriver by lazy { driverFactory.createDriver() }
    
    val database: AppDatabase by lazy {
        AppDatabase(driver).also {
            // Enable foreign key constraints (required for CASCADE/RESTRICT to work)
            driver.execute(null, "PRAGMA foreign_keys=ON", 0)
        }
    }
    
    // Convenience accessors for all query interfaces
    val categoryQueries get() = database.categoryQueries
    val friendQueries get() = database.friendQueries
    val expenseQueries get() = database.expenseQueries
    val expenseParticipantQueries get() = database.expenseParticipantQueries
    val monthlyBudgetQueries get() = database.monthlyBudgetQueries
    val categoryBudgetQueries get() = database.categoryBudgetQueries
    val monthStateQueries get() = database.monthStateQueries
    
    /**
     * Close the database connection.
     * Should be called when the database is no longer needed.
     */
    fun close() {
        driver.close()
    }
    
    /**
     * Execute a transaction with the database.
     * Automatically rolls back on exception.
     */
    fun <R> transaction(block: () -> R): R {
        return database.transactionWithResult {
            block()
        }
    }
}
