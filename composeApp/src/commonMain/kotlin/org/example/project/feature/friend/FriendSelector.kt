package org.example.project.feature.friend

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import org.example.project.domain.model.FriendModel
import org.example.project.domain.repository.FriendRepository

@OptIn(FlowPreview::class)
class FriendSelector(
    private val friendRepository: FriendRepository
) {
    private val _state = MutableStateFlow(FriendSelectionState())
    val state: StateFlow<FriendSelectionState> = _state.asStateFlow()
    
    private val searchQueryFlow = MutableStateFlow("")
    
    fun initialise(coroutineScope: CoroutineScope) {
        val allFriendsFlow = friendRepository
            .observeAllFriends()
            .onStart { emit(emptyList()) }
            .catch { emit(emptyList()) }
        
        val searchResult = searchQueryFlow
            .debounce(200)
            .flatMapLatest { query ->
                allFriendsFlow.map { allFriends ->
                    if (query.isBlank()) null
                    else allFriends.filter { it.name.contains(query, ignoreCase = true) }
                }
            }
            .distinctUntilChanged()
        
        combine(
            flow = allFriendsFlow,
            flow2 = searchQueryFlow,
            flow3 = searchResult
        ) { allFriends, searchQuery, searchResult ->
            _state.update { currentState ->
                currentState.copy(
                    allFriends = allFriends,
                    searchQuery = searchQuery,
                    searchResults = searchResult ?: emptyList(),
                    showAddOption = (searchResult != null && searchResult.isEmpty()),
                    validationError = validateInputQuery(searchQuery)
                )
            }
        }.launchIn(coroutineScope)
    }
    
    private fun validateInputQuery(query: String): String? {
        if (query.isBlank()) return null
        
        return when (val result = FriendValidation.validateFriendName(query)) {
            is FriendValidation.ValidationResult.Valid -> null
            is FriendValidation.ValidationResult.Invalid -> result.message
        }
    }
    
    fun onSearchQueryChanged(query: String) {
        searchQueryFlow.value = query
    }
    
    fun onFriendToggled(friend: FriendModel) {
        _state.update { currentState ->
            val isAlreadySelected = currentState.selectedFriends.any { it.id == friend.id }
            val updatedSelectedFriends = if (isAlreadySelected) {
                currentState.selectedFriends.filter { it.id != friend.id }
            } else {
                currentState.selectedFriends + friend
            }
            
            currentState.copy(selectedFriends = updatedSelectedFriends)
        }
    }
    
    suspend fun onAddNewFriend() {
        val query = _state.value.searchQuery.trim()
        
        if (query.isEmpty()) return
        
        val validationResult = FriendValidation.validateFriendName(query)
        if (validationResult is FriendValidation.ValidationResult.Invalid) {
            _state.update { it.copy(validationError = validationResult.message) }
            return
        }
        
        try {
            _state.update { it.copy(isLoading = true, validationError = null) }
            
            val friend = friendRepository.addFriend(query)
            
            _state.update { currentState ->
                currentState.copy(
                    isLoading = false,
                    recentlyAdded = friend,
                    selectedFriends = currentState.selectedFriends + friend
                )
            }
            searchQueryFlow.value = ""
        } catch (e: IllegalArgumentException) {
            _state.update { 
                it.copy(
                    isLoading = false, 
                    validationError = e.message ?: "Failed to add friend"
                ) 
            }
        } catch (e: Exception) {
            _state.update { 
                it.copy(
                    isLoading = false, 
                    validationError = "Failed to add friend"
                ) 
            }
        }
    }
    
    fun onClearSearch() {
        searchQueryFlow.value = ""
        _state.update { it.copy(validationError = null) }
    }
    
    fun clearSelection() {
        _state.update { it.copy(selectedFriends = emptyList()) }
    }
    
    fun clearValidationError() {
        _state.update { it.copy(validationError = null) }
    }
    
    fun reset() {
        searchQueryFlow.value = ""
        _state.update {
            FriendSelectionState(
                allFriends = it.allFriends
            )
        }
    }
}
