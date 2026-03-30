package org.example.project.domain.model

data class CategorySpendDetail(
    val category: CategoryModel,
    val totalAmount: Long,
    val transactions: List<ExpenseDetailModel>
)
