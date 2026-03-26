package org.example.project.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object Home : Screen

    @Serializable
    data object AddExpense : Screen

    @Serializable
    data class EditExpense(val expenseId: Long) : Screen

    @Serializable
    data object Friends : Screen
    
    @Serializable
    data class AddFriend(val friendId: Long? = null) : Screen
    
    @Serializable
    data object Categories : Screen
    
    @Serializable
    data class MonthlyReport(val year: Int, val month: Int) : Screen
    
    @Serializable
    data class FriendBalance(val friendId: Long) : Screen
    
    @Serializable
    data object Settings : Screen
}
