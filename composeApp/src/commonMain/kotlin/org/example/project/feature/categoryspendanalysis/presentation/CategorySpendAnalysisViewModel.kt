package org.example.project.feature.categoryspendanalysis.presentation

import org.example.project.feature.categoryspendanalysis.domain.CategorySpendAnalysisUseCase
import org.example.project.navigation.NavigationManager
import org.example.project.navigation.Screen
import org.example.project.ui.base.BaseViewModel

class CategorySpendAnalysisViewModel(
    private val categorySpendAnalysisUseCase: CategorySpendAnalysisUseCase,
    private val navigationManager: NavigationManager
) : BaseViewModel<CategorySpendAnalysisState, CategorySpendAnalysisIntent>(CategorySpendAnalysisState()) {

    override fun onIntent(intent: CategorySpendAnalysisIntent) {
        when (intent) {
            is CategorySpendAnalysisIntent.Initialise -> handleInitialise(intent.month, intent.year)
            CategorySpendAnalysisIntent.BackClicked -> handleBackClicked()
            is CategorySpendAnalysisIntent.CategoryRowTapped -> handleCategoryRowTapped(intent.categoryId)
            is CategorySpendAnalysisIntent.ExpenseClicked -> handleExpenseClicked(intent.expenseId)
        }
    }

    private fun handleInitialise(month: Int, year: Int) {
        // TODO: subscribe to categorySpendAnalysisUseCase(month, year) and update state
    }

    private fun handleBackClicked() {
        // TODO: navigationManager.navigateBack()
    }

    private fun handleCategoryRowTapped(categoryId: Long) {
        // TODO: toggle expandedCategoryId — set to null if already expanded, else set to categoryId
    }

    private fun handleExpenseClicked(expenseId: Long) {
        // TODO: navigationManager.navigateTo(Screen.EditExpense(expenseId))
    }
}
