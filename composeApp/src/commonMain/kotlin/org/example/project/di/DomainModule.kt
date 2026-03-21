package org.example.project.di

import org.example.project.feature.addexpense.domain.AddExpenseUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { AddExpenseUseCase(get()) }
}
