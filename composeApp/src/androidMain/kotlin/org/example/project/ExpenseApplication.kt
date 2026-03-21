package org.example.project

import android.app.Application
import org.example.project.di.androidModule
import org.example.project.di.commonModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ExpenseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        startKoin {
            androidContext(this@ExpenseApplication)
            modules(androidModule(this@ExpenseApplication) + commonModules())
        }
    }
}
