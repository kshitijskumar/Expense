package org.example.project.di

import org.example.project.domain.usecase.AddExpenseUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { AddExpenseUseCase(get()) }
}
