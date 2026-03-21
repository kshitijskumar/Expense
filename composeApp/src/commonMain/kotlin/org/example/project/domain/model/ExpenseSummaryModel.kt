package org.example.project.domain.model

data class ExpenseSummaryModel(
    val id: Long,
    val title: String,
    val amount: Long,
    val date: Long,
    val category: CategoryModel,
    val participantCount: Int,
    val notes: String?
)
