package org.example.project.feature.monthlyreport.domain.model

import org.example.project.domain.model.CategoryModel

/**
 * Spending data for a single category within a month.
 *
 * @param category The category details (id, name)
 * @param totalAmount Total spent in this category for the month
 */
data class CategorySpend(
    val category: CategoryModel,
    val totalAmount: Long
)
