package org.example.project.data.datasource

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.example.project.db.DatabaseHelper
import org.example.project.domain.model.AddExpenseInput
import org.example.project.domain.model.CategoryModel
import org.example.project.domain.model.ExpenseDetailModel
import org.example.project.domain.model.ExpenseSummaryModel
import org.example.project.domain.model.FriendModel

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

    suspend fun getExpenseById(expenseId: Long): ExpenseDetailModel? = withContext(Dispatchers.IO) {
        val expense = db.expenseQueries.getExpenseById(expenseId).executeAsOneOrNull() ?: return@withContext null

        val participants = db.expenseParticipantQueries.getParticipantsByExpense(expenseId)
            .executeAsList()
            .map { friendRow ->
                FriendModel(
                    id = friendRow.id,
                    name = friendRow.name
                )
            }

        ExpenseDetailModel(
            id = expense.id,
            title = expense.title,
            amount = expense.amount,
            date = expense.date,
            category = CategoryModel(
                id = expense.category_id,
                name = expense.category_name
            ),
            participants = participants,
            notes = expense.notes
        )
    }

    suspend fun updateExpense(expenseId: Long, input: AddExpenseInput) = withContext(Dispatchers.IO) {
        db.database.transaction {
            db.expenseQueries.updateExpense(
                id = expenseId,
                title = input.title,
                amount = input.amount,
                date = input.date,
                category_id = input.category.id,
                notes = input.notes
            )

            db.expenseParticipantQueries.deleteParticipantsByExpense(expenseId)

            input.participantFriends.forEach { friend ->
                db.expenseParticipantQueries.insertParticipant(
                    expense_id = expenseId,
                    friend_id = friend.id
                )
            }
        }
    }

    suspend fun deleteExpense(expenseId: Long) = withContext(Dispatchers.IO) {
        db.database.transaction {
            db.expenseParticipantQueries.deleteParticipantsByExpense(expenseId)
            db.expenseQueries.deleteExpense(expenseId)
        }
    }

    suspend fun getLatestTransactionDate(): Long? = withContext(Dispatchers.IO) {
        db.expenseQueries.getLatestTransactionDate().executeAsOneOrNull()?.MAX
    }

    suspend fun getExpensesByDate(startOfDay: Long, endOfDay: Long): List<ExpenseSummaryModel> =
        withContext(Dispatchers.IO) {
            db.expenseQueries.getExpensesByDate(startOfDay, endOfDay)
                .executeAsList()
                .map { expense ->
                    val participantCount = db.expenseParticipantQueries
                        .getParticipantsByExpense(expense.id)
                        .executeAsList()
                        .size

                    ExpenseSummaryModel(
                        id = expense.id,
                        title = expense.title,
                        amount = expense.amount,
                        date = expense.date,
                        category = CategoryModel(
                            id = expense.category_id,
                            name = expense.category_name
                        ),
                        participantCount = participantCount,
                        notes = expense.notes
                    )
                }
        }

    suspend fun getTotalSpentForMonth(monthStart: Long, monthEnd: Long): Long =
        withContext(Dispatchers.IO) {
            db.expenseQueries.getTotalExpensesByMonth(monthStart, monthEnd)
                .executeAsOneOrNull()
                ?.total
                ?: 0L
        }

    fun getLatestTransactionDateFlow(): Flow<Long?> {
        return db.expenseQueries.getLatestTransactionDate()
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .map { row -> row?.MAX }
    }

    fun getExpensesByDateFlow(startOfDay: Long, endOfDay: Long): Flow<List<ExpenseSummaryModel>> {
        return db.expenseQueries.getExpensesByDate(startOfDay, endOfDay)
            .asFlow()
            .mapToList(Dispatchers.IO)
            .map { expenses ->
                expenses.map { expense ->
                    val participantCount = db.expenseParticipantQueries
                        .getParticipantsByExpense(expense.id)
                        .executeAsList()
                        .size

                    ExpenseSummaryModel(
                        id = expense.id,
                        title = expense.title,
                        amount = expense.amount,
                        date = expense.date,
                        category = CategoryModel(
                            id = expense.category_id,
                            name = expense.category_name
                        ),
                        participantCount = participantCount,
                        notes = expense.notes
                    )
                }
            }
    }

    fun getTotalSpentForMonthFlow(monthStart: Long, monthEnd: Long): Flow<Long> {
        return db.expenseQueries.getTotalExpensesByMonth(monthStart, monthEnd)
            .asFlow()
            .mapToOneOrNull(Dispatchers.IO)
            .map { row -> row?.total ?: 0L }
    }
}
