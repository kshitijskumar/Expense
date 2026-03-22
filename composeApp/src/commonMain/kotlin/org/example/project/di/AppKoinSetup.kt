package org.example.project.di

fun commonModules() = listOf(
    databaseModule,
    dataModule,
    domainModule,
    navigationModule,
    presentationModule
)
