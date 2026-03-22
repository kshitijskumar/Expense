package org.example.project.data.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.example.project.db.DatabaseHelper
import org.example.project.db.Friend

class FriendLocalDataSource(private val db: DatabaseHelper) {
    
    fun observeAllFriends(): Flow<List<Friend>> {
        return db.friendQueries
            .getAllFriends()
            .asFlow()
            .mapToList(Dispatchers.IO)
    }
    
    suspend fun friendExists(name: String): Boolean = withContext(Dispatchers.IO) {
        db.friendQueries.friendExistsByName(name).executeAsOne() == 1L
    }
    
    suspend fun insertFriend(name: String): Friend = withContext(Dispatchers.IO) {
        db.database.transactionWithResult {
            db.friendQueries.insertFriend(name)
            db.friendQueries.lastInsertedFriend().executeAsOne()
        }
    }
}
