package org.example.project.feature.category

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
import org.example.project.domain.model.CategoryModel
import org.example.project.domain.repository.CategoryRepository

@OptIn(FlowPreview::class)
class CategorySelector(
    private val categoryRepository: CategoryRepository
) {
    private val _state = MutableStateFlow(CategorySelectionState())
    val state: StateFlow<CategorySelectionState> = _state.asStateFlow()
    private val searchQueryFlow = MutableStateFlow("")
    
    fun initialise(coroutineScope: CoroutineScope) {
        val allCategoriesFlow = categoryRepository
            .observeAllCategories()
            .onStart { emit(listOf()) }
            .catch { e ->
                emit(emptyList())
            }

        val searchResult = searchQueryFlow
            .debounce(200)
            .flatMapLatest { query ->
                allCategoriesFlow.map { allCategories ->
                    if (query.isBlank()) {
                        null
                    } else {
                        val filtered = allCategories.filter { it.name.contains(query, ignoreCase = true) }
                        filtered
                    }
                }
            }
            .distinctUntilChanged()

        combine(
            flow = allCategoriesFlow,
            flow2 = searchQueryFlow,
            flow3 = searchResult
        ) { allCategories, searchQuery, searchResult ->
            _state.update {
                it.copy(
                    basicCategories = allCategories.take(5),
                    searchQuery = searchQuery,
                    showAddOption = (searchResult != null && searchResult.isEmpty()),
                    validationError = validateInputQuery(searchQuery),
                    searchResults = searchResult ?: listOf()
                )
            }
        }.launchIn(coroutineScope)
    }

    private fun validateInputQuery(query: String): String? {
        if (query.isEmpty()) {
            return null
        }

        return when(val result = CategoryValidation.validateCategoryName(query)) {
            is CategoryValidation.ValidationResult.Invalid -> result.message
            CategoryValidation.ValidationResult.Valid -> null
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchQueryFlow.update { query }
    }
    
    fun onCategorySelected(category: CategoryModel) {
        _state.update { 
            it.copy(
                selectedCategory = category
            )
        }
        searchQueryFlow.update { "" }
    }
    
    suspend fun onAddNewCategory() {
        val query = _state.value.searchQuery.trim()
        
        // Validate again (should already be validated, but defensive check)
        when (val result = CategoryValidation.validateCategoryName(query)) {
            is CategoryValidation.ValidationResult.Invalid -> {
                _state.update { it.copy(validationError = result.message) }
                return
            }
            is CategoryValidation.ValidationResult.Valid -> {}
        }

        _state.update { it.copy(isLoading = true) }

        try {
            val category = categoryRepository.addCategory(query)
            _state.update {
                it.copy(
                    isLoading = false,
                    recentlyAdded = category,
                    selectedCategory = category
                )
            }
            searchQueryFlow.value = ""
        } catch (e: IllegalArgumentException) {
            _state.update {
                it.copy(isLoading = false, validationError = e.message)
            }
        } catch (e: Exception) {
            _state.update {
                it.copy(isLoading = false, validationError = "Failed to add category")
            }
        }
    }
    
    fun onClearSearch() {
        searchQueryFlow.value = ""
    }
    
    fun clearSelection() {
        _state.update { it.copy(selectedCategory = null) }
    }
    
    fun clearValidationError() {
        _state.update { it.copy(validationError = null) }
    }
    
    fun reset() {
        _state.update { 
            CategorySelectionState(basicCategories = _state.value.basicCategories) 
        }
        searchQueryFlow.value = ""
    }
}
