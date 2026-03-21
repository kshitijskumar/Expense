package org.example.project.di

import org.example.project.data.datasource.ExpenseLocalDataSource
import org.example.project.data.repository.ExpenseRepositoryImpl
import org.example.project.domain.repository.ExpenseRepository
import org.koin.dsl.module

val dataModule = module {
    single { ExpenseLocalDataSource(get()) }
    single<ExpenseRepository> { ExpenseRepositoryImpl(get()) }
}
