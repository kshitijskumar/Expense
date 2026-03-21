package org.example.project.di

import android.content.Context
import org.example.project.db.AndroidDatabaseDriverFactory
import org.example.project.db.DatabaseDriverFactory
import org.koin.dsl.module

fun androidModule(context: Context) = module {
    single<DatabaseDriverFactory> { AndroidDatabaseDriverFactory(context) }
}
