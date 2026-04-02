package org.example.project.di

import org.example.project.feature.addexpense.domain.AddExpenseUseCase
import org.example.project.feature.addexpense.domain.DeleteExpenseUseCase
import org.example.project.feature.addexpense.domain.ExpenseInputValidation
import org.example.project.feature.addexpense.domain.ExpenseInputValidationImpl
import org.example.project.feature.addexpense.domain.GetExpenseUseCase
import org.example.project.feature.addexpense.domain.UpdateExpenseUseCase
import org.example.project.feature.home.domain.orchestrator.HomeOrchestrator
import org.example.project.feature.home.domain.orchestrator.HomeOrchestratorImpl
import org.example.project.feature.categoryspendanalysis.domain.CategorySpendAnalysisUseCase
import org.example.project.feature.categoryspendanalysis.domain.CategorySpendAnalysisUseCaseImpl
import org.example.project.feature.monthlyreport.domain.ComputeFriendSpendsUseCase
import org.example.project.feature.monthlyreport.domain.MonthlyReportOrchestrator
import org.example.project.feature.monthlyreport.domain.MonthlyReportOrchestratorImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val domainModule = module {
    factory {
        AddExpenseUseCase(
            expenseRepository = get(),
            validation = get()
        )
    }

    factory {
        GetExpenseUseCase(
            expenseRepository = get()
        )
    }

    factory {
        UpdateExpenseUseCase(
            expenseRepository = get(),
            validation = get()
        )
    }

    factory {
        DeleteExpenseUseCase(
            expenseRepository = get()
        )
    }

    factory<HomeOrchestrator> { HomeOrchestratorImpl(get(), get()) }

    factoryOf(::CategorySpendAnalysisUseCaseImpl) { bind<CategorySpendAnalysisUseCase>() }
    factoryOf(::ComputeFriendSpendsUseCase)
    factory<MonthlyReportOrchestrator> { MonthlyReportOrchestratorImpl(get(), get()) }

    factoryOf(::ExpenseInputValidationImpl) { bind<ExpenseInputValidation>() }
}
