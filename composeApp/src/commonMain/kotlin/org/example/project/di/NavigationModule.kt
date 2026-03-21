package org.example.project.di

import org.example.project.navigation.NavigationManager
import org.koin.dsl.module

val navigationModule = module {
    single { NavigationManager() }
}
