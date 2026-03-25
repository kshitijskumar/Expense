package org.example.project.di

import org.example.project.feature.addexpense.presentation.AddExpenseViewModel
import org.example.project.feature.addexpense.presentation.EditExpenseViewModel
import org.example.project.feature.home.presentation.HomeViewModel
import org.example.project.feature.category.CategorySelector
import org.example.project.feature.friend.FriendSelector
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::AddExpenseViewModel)
    viewModelOf(::HomeViewModel)

    factory { (expenseId: Long) ->
        EditExpenseViewModel(
            expenseId = expenseId,
            getExpenseUseCase = get(),
            updateExpenseUseCase = get(),
            deleteExpenseUseCase = get(),
            navigationManager = get(),
            categorySelector = get(),
            friendSelector = get()
        )
    }

    factoryOf(::CategorySelector)
    factoryOf(::FriendSelector)
}
