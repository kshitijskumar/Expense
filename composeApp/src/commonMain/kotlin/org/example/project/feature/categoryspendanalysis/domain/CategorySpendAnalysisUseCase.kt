package org.example.project.feature.categoryspendanalysis.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import org.example.project.domain.model.CategorySpendDetail
import org.example.project.domain.repository.ExpenseRepository

class CategorySpendAnalysisUseCase(
    private val expenseRepository: ExpenseRepository
) {
    operator fun invoke(month: Int, year: Int): Flow<List<CategorySpendDetail>> {
        return emptyFlow()
    }
}
