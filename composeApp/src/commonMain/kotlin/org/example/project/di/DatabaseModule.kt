package org.example.project.di

import org.example.project.db.DatabaseHelper
import org.koin.dsl.module

val databaseModule = module {
    single { DatabaseHelper(get()) }
}
