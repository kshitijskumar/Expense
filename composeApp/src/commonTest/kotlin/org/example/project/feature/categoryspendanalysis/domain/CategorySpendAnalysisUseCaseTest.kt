package org.example.project.feature.categoryspendanalysis.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.example.project.domain.model.AddExpenseInput
import org.example.project.domain.model.CategoryModel
import org.example.project.domain.model.ExpenseDetailModel
import org.example.project.domain.model.ExpenseSummaryModel
import org.example.project.domain.repository.ExpenseRepository
import org.example.project.util.DateTimeUtil
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CategorySpendAnalysisUseCaseTest {

    private val fakeRepository = FakeExpenseRepository()
    private val useCase = CategorySpendAnalysisUseCaseImpl(fakeRepository)

    // region Test 1 — correct month range passed to repository

    @Test
    fun `when invoked - repository is called with correct month start and end timestamps`() = runTest {
        val month = 3
        val year = 2024
        val (expectedStart, expectedEnd) = DateTimeUtil.getMonthRange(month, year)
        fakeRepository.emit(emptyList())

        useCase(month, year).take(1).toList()

        assertEquals(expectedStart, fakeRepository.capturedMonthStart)
        assertEquals(expectedEnd, fakeRepository.capturedMonthEnd)
    }

    // endregion

    // region Test 2 — descending sort by total spend

    @Test
    fun `when invoked - output list is sorted descending by total spend`() = runTest {
        val catFood = CategoryModel(1L, "Food")
        val catTravel = CategoryModel(2L, "Travel")
        val catEntertainment = CategoryModel(3L, "Entertainment")

        fakeRepository.emit(
            listOf(
                expense(1L, 100L, catFood),
                expense(2L, 300L, catTravel),
                expense(3L, 200L, catEntertainment),
            )
        )

        val result = useCase(3, 2024).take(1).toList().first()

        assertEquals(listOf(300L, 200L, 100L), result.map { it.totalAmount })
    }

    // endregion

    // region Test 3 — zero-spend categories excluded

    @Test
    fun `when invoked - output list contains only categories with total spend greater than 0`() = runTest {
        val catFood = CategoryModel(1L, "Food")
        val catZero = CategoryModel(2L, "ZeroSpend")

        fakeRepository.emit(
            listOf(
                expense(1L, 100L, catFood),
                expense(2L, 0L, catZero),
            )
        )

        val result = useCase(3, 2024).take(1).toList().first()

        assertTrue(result.all { it.totalAmount > 0 })
        assertFalse(result.any { it.category == catZero })
    }

    // endregion

    // region Test 4 — each category group holds only its own expenses

    @Test
    fun `when invoked - each category group holds only expenses of that category`() = runTest {
        val catFood = CategoryModel(1L, "Food")
        val catTravel = CategoryModel(2L, "Travel")

        val foodExpense1 = expense(1L, 100L, catFood)
        val foodExpense2 = expense(2L, 150L, catFood)
        val travelExpense = expense(3L, 200L, catTravel)

        fakeRepository.emit(listOf(foodExpense1, foodExpense2, travelExpense))

        val result = useCase(3, 2024).take(1).toList().first()

        val foodDetail = result.first { it.category == catFood }
        val travelDetail = result.first { it.category == catTravel }

        assertTrue(foodDetail.transactions.all { it.category == catFood })
        assertTrue(travelDetail.transactions.all { it.category == catTravel })
        assertEquals(2, foodDetail.transactions.size)
        assertEquals(1, travelDetail.transactions.size)
    }

    // endregion

    // region Test 5 — no expenses returns empty list

    @Test
    fun `when invoked and no expenses recorded for the month - returns flow of empty list`() = runTest {
        fakeRepository.emit(emptyList())

        val result = useCase(3, 2024).take(1).toList().first()

        assertTrue(result.isEmpty())
    }

    // endregion

    // region Test 6 — totalAmount is correct sum of category transactions

    @Test
    fun `when invoked - totalAmount for each category is sum of its transaction amounts`() = runTest {
        val catFood = CategoryModel(1L, "Food")
        val catTravel = CategoryModel(2L, "Travel")

        fakeRepository.emit(
            listOf(
                expense(1L, 100L, catFood),
                expense(2L, 250L, catFood),
                expense(3L, 180L, catTravel),
            )
        )

        val result = useCase(3, 2024).take(1).toList().first()

        val foodDetail = result.first { it.category == catFood }
        val travelDetail = result.first { it.category == catTravel }

        assertEquals(350L, foodDetail.totalAmount)
        assertEquals(180L, travelDetail.totalAmount)
    }

    // endregion

    // region Test 7 — flow re-emits when repository emits new data

    @Test
    fun `when invoked - new repository emission causes flow to re-emit updated results`() = runTest {
        val catFood = CategoryModel(1L, "Food")

        fakeRepository.emit(listOf(expense(1L, 100L, catFood)))

        val emissions = mutableListOf<List<org.example.project.domain.model.CategorySpendDetail>>()
        val job = launch {
            useCase(3, 2024).take(2).toList(emissions)
        }

        advanceUntilIdle() // let collector start and receive first emission

        fakeRepository.emit(
            listOf(
                expense(1L, 100L, catFood),
                expense(2L, 200L, catFood),
            )
        )

        advanceUntilIdle() // let collector receive second emission and take(2) complete
        job.join()

        assertEquals(2, emissions.size)
        assertEquals(100L, emissions[0].first().totalAmount)
        assertEquals(300L, emissions[1].first().totalAmount)
    }

    // endregion
}

// region Helpers

private fun expense(
    id: Long,
    amount: Long,
    category: CategoryModel,
    date: Long = 0L,
) = ExpenseDetailModel(
    id = id,
    title = "Expense $id",
    amount = amount,
    date = date,
    category = category,
    participants = emptyList(),
    notes = null
)

private class FakeExpenseRepository : ExpenseRepository {

    var capturedMonthStart: Long = 0L
        private set
    var capturedMonthEnd: Long = 0L
        private set

    private val expensesFlow = MutableStateFlow<List<ExpenseDetailModel>>(emptyList())

    fun emit(expenses: List<ExpenseDetailModel>) {
        expensesFlow.value = expenses
    }

    override fun getExpensesWithParticipantsForMonthFlow(
        monthStart: Long,
        monthEnd: Long
    ): Flow<List<ExpenseDetailModel>> {
        capturedMonthStart = monthStart
        capturedMonthEnd = monthEnd
        return expensesFlow
    }

    override suspend fun addExpense(input: AddExpenseInput) = throw NotImplementedError()
    override suspend fun getExpenseById(expenseId: Long): ExpenseDetailModel? = throw NotImplementedError()
    override suspend fun updateExpense(expenseId: Long, input: AddExpenseInput) = throw NotImplementedError()
    override suspend fun deleteExpense(expenseId: Long) = throw NotImplementedError()
    override suspend fun getLatestTransactionDate(): Long? = throw NotImplementedError()
    override suspend fun getExpensesByDate(startOfDay: Long, endOfDay: Long): List<ExpenseSummaryModel> = throw NotImplementedError()
    override suspend fun getTotalSpentForMonth(monthStart: Long, monthEnd: Long): Long = throw NotImplementedError()
    override fun getLatestTransactionDateFlow(): Flow<Long?> = throw NotImplementedError()
    override fun getExpensesByDateFlow(startOfDay: Long, endOfDay: Long): Flow<List<ExpenseSummaryModel>> = throw NotImplementedError()
    override fun getTotalSpentForMonthFlow(monthStart: Long, monthEnd: Long): Flow<Long> = throw NotImplementedError()
}

// endregion
