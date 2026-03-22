package org.example.project.data.datasource

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import org.example.project.db.DatabaseHelper
import org.example.project.domain.model.AddExpenseInput

class ExpenseLocalDataSource(private val db: DatabaseHelper) {
    
    suspend fun insertWithParticipants(input: AddExpenseInput) = withContext(Dispatchers.IO) {
        db.database.transaction {
            db.expenseQueries.insertExpense(
                title = input.title,
                amount = input.amount,
                date = input.date,
                category_id = input.category.id,
                notes = input.notes
            )
            
            val expenseId = db.expenseQueries.lastInsertRowId().executeAsOne()
            
            input.participantFriends.forEach { friend ->
                db.expenseParticipantQueries.insertParticipant(
                    expense_id = expenseId,
                    friend_id = friend.id
                )
            }
        }
    }
}
