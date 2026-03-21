package org.example.project.di

import org.example.project.db.DatabaseDriverFactory
import org.example.project.db.IosDatabaseDriverFactory
import org.koin.dsl.module

fun iosModule() = module {
    single<DatabaseDriverFactory> { IosDatabaseDriverFactory() }
}
