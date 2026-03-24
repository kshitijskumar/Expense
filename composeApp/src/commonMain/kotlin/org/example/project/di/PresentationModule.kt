package org.example.project.di

import org.example.project.feature.addexpense.presentation.AddExpenseViewModel
import org.example.project.feature.home.presentation.HomeViewModel
import org.example.project.feature.category.CategorySelector
import org.example.project.feature.friend.FriendSelector
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::AddExpenseViewModel)
    viewModelOf(::HomeViewModel)
    factoryOf(::CategorySelector)
    factoryOf(::FriendSelector)
}
