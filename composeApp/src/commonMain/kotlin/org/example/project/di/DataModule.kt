package org.example.project.di

import org.example.project.data.datasource.CategoryLocalDataSource
import org.example.project.data.datasource.ExpenseLocalDataSource
import org.example.project.data.datasource.FriendLocalDataSource
import org.example.project.data.datasource.MonthlyBudgetLocalDataSource
import org.example.project.data.repository.CategoryRepositoryImpl
import org.example.project.data.repository.ExpenseRepositoryImpl
import org.example.project.data.repository.FriendRepositoryImpl
import org.example.project.data.repository.MonthlyBudgetRepositoryImpl
import org.example.project.domain.repository.CategoryRepository
import org.example.project.domain.repository.ExpenseRepository
import org.example.project.domain.repository.FriendRepository
import org.example.project.domain.repository.MonthlyBudgetRepository
import org.koin.dsl.module

val dataModule = module {
    single { ExpenseLocalDataSource(get()) }
    single { CategoryLocalDataSource(get()) }
    single { FriendLocalDataSource(get()) }
    single { MonthlyBudgetLocalDataSource(get()) }

    single<ExpenseRepository> { ExpenseRepositoryImpl(get()) }
    single<MonthlyBudgetRepository> { MonthlyBudgetRepositoryImpl(get()) }
    single<CategoryRepository> { CategoryRepositoryImpl(get()) }
    single<FriendRepository> { FriendRepositoryImpl(get()) }
}
