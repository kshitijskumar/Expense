package org.example.project.feature.categoryspendanalysis.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.example.project.feature.categoryspendanalysis.domain.CategorySpendAnalysisUseCase
import org.example.project.navigation.NavigationManager
import org.example.project.navigation.Screen
import org.example.project.ui.base.BaseViewModel

class CategorySpendAnalysisViewModel(
    args: Screen.CategorySpendAnalysis,
    private val categorySpendAnalysisUseCase: CategorySpendAnalysisUseCase,
    private val navigationManager: NavigationManager
) : BaseViewModel<CategorySpendAnalysisState, CategorySpendAnalysisIntent>(CategorySpendAnalysisState()) {

    init {
        onIntent(CategorySpendAnalysisIntent.Initialise(month = args.month, year = args.year))
    }

    override fun onIntent(intent: CategorySpendAnalysisIntent) {
        when (intent) {
            is CategorySpendAnalysisIntent.Initialise -> handleInitialise(intent.month, intent.year)
            CategorySpendAnalysisIntent.BackClicked -> handleBackClicked()
            is CategorySpendAnalysisIntent.CategoryRowTapped -> handleCategoryRowTapped(intent.categoryId)
            is CategorySpendAnalysisIntent.ExpenseClicked -> handleExpenseClicked(intent.expenseId)
        }
    }

    private fun handleInitialise(month: Int, year: Int) {
        categorySpendAnalysisUseCase(month, year)
            .onEach { details ->
                updateState { copy(isLoading = false, categorySpendDetails = details) }
            }
            .launchIn(viewModelScope)
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
