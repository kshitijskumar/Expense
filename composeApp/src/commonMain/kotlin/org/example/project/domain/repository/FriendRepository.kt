package org.example.project.domain.repository

import kotlinx.coroutines.flow.Flow
import org.example.project.domain.model.FriendModel

interface FriendRepository {
    fun observeAllFriends(): Flow<List<FriendModel>>
    suspend fun addFriend(name: String): FriendModel
}
