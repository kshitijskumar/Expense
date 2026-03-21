package org.example.project.domain.repository

import org.example.project.domain.model.FriendModel

interface FriendRepository {
    suspend fun getAllFriends(): List<FriendModel>
}
