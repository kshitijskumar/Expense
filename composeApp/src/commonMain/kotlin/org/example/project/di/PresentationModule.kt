package org.example.project.di

import org.example.project.feature.addexpense.presentation.AddExpenseViewModel
import org.example.project.feature.category.CategorySelector
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {
    viewModelOf(::AddExpenseViewModel)
    factoryOf(::CategorySelector)
}
