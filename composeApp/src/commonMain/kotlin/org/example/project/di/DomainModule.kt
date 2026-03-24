package org.example.project.di

import org.example.project.feature.addexpense.domain.AddExpenseUseCase
import org.example.project.feature.home.domain.orchestrator.HomeOrchestrator
import org.example.project.feature.home.domain.orchestrator.HomeOrchestratorImpl
import org.koin.dsl.module

val domainModule = module {
    factory { AddExpenseUseCase(get()) }

    factory<HomeOrchestrator> { HomeOrchestratorImpl(get(), get()) }
}
