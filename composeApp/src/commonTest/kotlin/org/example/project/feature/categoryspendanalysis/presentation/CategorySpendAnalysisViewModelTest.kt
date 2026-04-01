package org.example.project.feature.categoryspendanalysis.presentation

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.example.project.domain.model.CategoryModel
import org.example.project.domain.model.CategorySpendDetail
import org.example.project.feature.categoryspendanalysis.domain.CategorySpendAnalysisUseCase
import org.example.project.navigation.NavigationManager
import org.example.project.navigation.Screen
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CategorySpendAnalysisViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val fakeUseCase = FakeCategorySpendAnalysisUseCase()
    private val navigationManager = NavigationManager()
    private lateinit var viewModel: CategorySpendAnalysisViewModel

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = CategorySpendAnalysisViewModel(
            args = Screen.CategorySpendAnalysis(year = 2024, month = 3),
            categorySpendAnalysisUseCase = fakeUseCase,
            navigationManager = navigationManager
        )
    }

    @AfterTest
    fun teardown() {
        Dispatchers.resetMain()
    }

    // region Initialise intent

    @Test
    fun `on Initialise intent - state has isLoading true before use case emits`() = runTest {
        viewModel.onIntent(CategorySpendAnalysisIntent.Initialise(month = 3, year = 2024))

        assertTrue(viewModel.state.value.isLoading)
    }

    @Test
    fun `on Initialise intent - use case is invoked with correct month and year`() = runTest {
        viewModel.onIntent(CategorySpendAnalysisIntent.Initialise(month = 3, year = 2024))
        advanceUntilIdle()

        assertEquals(3, fakeUseCase.capturedMonth)
        assertEquals(2024, fakeUseCase.capturedYear)
    }

    @Test
    fun `on Initialise intent - on receiving data isLoading is false and categorySpendDetails is updated`() = runTest {
        val catFood = CategoryModel(1L, "Food")
        val details = listOf(CategorySpendDetail(catFood, 100L, emptyList()))

        viewModel.onIntent(CategorySpendAnalysisIntent.Initialise(month = 3, year = 2024))
        fakeUseCase.emit(details)
        advanceUntilIdle()

        assertFalse(viewModel.state.value.isLoading)
        assertEquals(details, viewModel.state.value.categorySpendDetails)
    }

    @Test
    fun `on Initialise intent - subsequent use case emissions continue to update categorySpendDetails`() = runTest {
        val catFood = CategoryModel(1L, "Food")
        val catTravel = CategoryModel(2L, "Travel")
        val firstEmission = listOf(CategorySpendDetail(catFood, 100L, emptyList()))
        val secondEmission = listOf(
            CategorySpendDetail(catTravel, 300L, emptyList()),
            CategorySpendDetail(catFood, 100L, emptyList()),
        )

        viewModel.onIntent(CategorySpendAnalysisIntent.Initialise(month = 3, year = 2024))
        fakeUseCase.emit(firstEmission)
        advanceUntilIdle()

        assertEquals(firstEmission, viewModel.state.value.categorySpendDetails)

        fakeUseCase.emit(secondEmission)
        advanceUntilIdle()

        assertEquals(secondEmission, viewModel.state.value.categorySpendDetails)
    }

    // endregion

    // TODO: T4 — CategoryRowTapped on a collapsed category sets expandedCategoryId to that id

    // TODO: T5 — CategoryRowTapped on the already expanded category collapses it (expandedCategoryId = null)

    // TODO: T6 — CategoryRowTapped on a different category replaces the currently expanded one

    // TODO: T7 — BackClicked triggers navigation back

    // TODO: T8 — ExpenseClicked navigates to Screen.EditExpense with correct expenseId
}

// region Fake

private class FakeCategorySpendAnalysisUseCase : CategorySpendAnalysisUseCase {

    var capturedMonth: Int = -1
        private set
    var capturedYear: Int = -1
        private set

    private val flow = MutableStateFlow<List<CategorySpendDetail>?>(null)

    fun emit(data: List<CategorySpendDetail>) {
        flow.value = data
    }

    override fun invoke(month: Int, year: Int): Flow<List<CategorySpendDetail>> {
        capturedMonth = month
        capturedYear = year
        return flow.filterNotNull()
    }
}

// endregion
