package org.example.project.feature.categoryspendanalysis.presentation

import org.example.project.domain.model.CategorySpendDetail

data class CategorySpendAnalysisState(
    val isLoading: Boolean = true,
    val categorySpendDetails: List<CategorySpendDetail> = emptyList(),
    val expandedCategoryId: Long? = null
)

sealed interface CategorySpendAnalysisIntent {
    data class Initialise(val month: Int, val year: Int) : CategorySpendAnalysisIntent
    data object BackClicked : CategorySpendAnalysisIntent
    data class CategoryRowTapped(val categoryId: Long) : CategorySpendAnalysisIntent
    data class ExpenseClicked(val expenseId: Long) : CategorySpendAnalysisIntent
}
