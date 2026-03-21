package org.example.project

import androidx.compose.ui.window.ComposeUIViewController
import org.example.project.di.commonModules
import org.example.project.di.iosModule
import org.koin.core.context.startKoin
import platform.UIKit.UIViewController

fun MainViewController(): UIViewController {
    startKoin {
        modules(iosModule() + commonModules())
    }
    return ComposeUIViewController { App() }
}