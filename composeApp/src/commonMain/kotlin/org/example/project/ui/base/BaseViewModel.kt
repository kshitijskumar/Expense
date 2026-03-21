package org.example.project.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Base ViewModel for MVI pattern implementation.
 *
 * @param S State type - represents the complete UI state for a screen
 * @param I Intent type - represents user actions/events on the screen
 * @param initialState The initial state when the ViewModel is created
 */
abstract class BaseViewModel<S, I>(
    initialState: S
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    /**
     * Update state immutably using a reducer function.
     * This ensures all state changes go through a single point and are predictable.
     *
     * Example usage:
     * ```
     * updateState { copy(isLoading = false, data = newData) }
     * ```
     */
    protected fun updateState(reducer: S.() -> S) {
        _state.update(reducer)
    }

    /**
     * Handle user intents/actions.
     * Subclasses must implement this to process screen-specific intents.
     *
     * Example implementation:
     * ```
     * override fun onIntent(intent: MyIntent) {
     *     when (intent) {
     *         is MyIntent.ButtonClicked -> handleButtonClick()
     *         is MyIntent.TextChanged -> handleTextChange(intent.text)
     *     }
     * }
     * ```
     */
    abstract fun onIntent(intent: I)
}
