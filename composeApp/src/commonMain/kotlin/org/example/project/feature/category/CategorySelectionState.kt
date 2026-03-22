package org.example.project.feature.category

import org.example.project.domain.model.CategoryModel

data class CategorySelectionState(
    val basicCategories: List<CategoryModel> = emptyList(),
    val recentlyAdded: CategoryModel? = null,
    val searchQuery: String = "",
    val searchResults: List<CategoryModel> = emptyList(),
    val showAddOption: Boolean = false,
    val isLoading: Boolean = false,
    val selectedCategory: CategoryModel? = null,
    val validationError: String? = null
)
