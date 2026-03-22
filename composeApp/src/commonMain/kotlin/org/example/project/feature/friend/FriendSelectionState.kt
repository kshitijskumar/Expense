package org.example.project.feature.friend

import org.example.project.domain.model.FriendModel

data class FriendSelectionState(
    val allFriends: List<FriendModel> = emptyList(),
    val selectedFriends: List<FriendModel> = emptyList(),
    val searchQuery: String = "",
    val searchResults: List<FriendModel> = emptyList(),
    val showAddOption: Boolean = false,
    val isLoading: Boolean = false,
    val validationError: String? = null,
    val recentlyAdded: FriendModel? = null
)
