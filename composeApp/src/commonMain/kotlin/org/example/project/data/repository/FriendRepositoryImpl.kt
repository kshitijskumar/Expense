package org.example.project.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.example.project.data.datasource.FriendLocalDataSource
import org.example.project.db.Friend
import org.example.project.domain.model.FriendModel
import org.example.project.domain.repository.FriendRepository

class FriendRepositoryImpl(
    private val dataSource: FriendLocalDataSource
) : FriendRepository {
    
    override fun observeAllFriends(): Flow<List<FriendModel>> {
        return dataSource.observeAllFriends().map { friends ->
            friends.map { it.toFriendModel() }
        }
    }
    
    override suspend fun addFriend(name: String): FriendModel {
        if (dataSource.friendExists(name)) {
            throw IllegalArgumentException("Friend with this name already exists")
        }
        return dataSource.insertFriend(name).toFriendModel()
    }
    
    private fun Friend.toFriendModel() = FriendModel(
        id = id,
        name = name
    )
}
