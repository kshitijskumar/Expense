### What
We need to build a category based spends analysis. The purpose is to be able to identify how much we spend
on each category in a month and be able to compare expenses.

## Feature details
- For a month show all categories in the descending order of expenses in that category
- Categories are shown in a collapsible/accordion fashion — tapping a category expands it inline to show the list of transactions made in that category, similar to MonthlyReportScreen transaction rows
- For all categories where spends > 0, there should be a horizontal bar chart where each bar (Y-axis) represents a category sorted in descending order of spends, and X-axis shows the spend amount

## Constraints
- Screen title: "Spend by Category"
- This should be created as a new screen, navigated to from MonthlyReportScreen categories card (replaces the existing "+ view all" / ViewAllCategoriesClicked behavior)
- The screen receives month and year as navigation args and re-fetches data independently
- No month navigation on this screen — it is locked to the month navigated from
- For the bar graph, ideally we should build it natively, but open to explore any 3rd party libraries if it simplifies things
- The screen/feature should follow the pattern we have in the project and not create a new pattern unless it benefits the feature
- Follow TDD format for the major usecases and sections, will decide this on the go with each phase of implementation

## Tasks

### Phase 1: Navigation & Entry Point
- T1: Add `Screen.CategorySpendAnalysis(year: Int, month: Int)` to the `Screen` sealed interface in Navigator
- T2: Register the new screen composable in NavHost
- T3: Update `MonthlyReportViewModel` — change `ViewAllCategoriesClicked` intent handling to navigate to `Screen.CategorySpendAnalysis` instead of the current behavior
  - Tests: unit test that `ViewAllCategoriesClicked` now emits a navigation event to `Screen.CategorySpendAnalysis` with correct month/year

### Phase 2: Data Layer
- T4: Create `CategorySpendDetail` domain model — holds `CategoryModel`, `totalAmount: Long`, and `transactions: List<ExpenseDetailModel>`
- T5: Create `CategorySpendAnalysisOrchestrator` interface and implementation — derives `List<CategorySpendDetail>` sorted descending by `totalAmount` by grouping results from the existing `getExpensesWithParticipantsForMonthFlow`, filtering out zero-spend categories
  - Tests (written first): grouping transactions by category, descending sort by total amount, zero-spend categories excluded, multiple transactions in same category summed correctly

### Phase 3: Presentation Layer
- T6: Create `CategorySpendAnalysisContract.kt` with `CategorySpendAnalysisState` (loading, list of `CategorySpendDetail`, set of expanded category ids) and `CategorySpendAnalysisIntent` (back, category row tapped to toggle expand)
- T7: Create `CategorySpendAnalysisViewModel` — accepts `Screen.CategorySpendAnalysis` args, subscribes to orchestrator flow, manages expand/collapse state
  - Tests (written first): initial loading state, state updated when orchestrator emits, toggling expand adds/removes category id from expanded set, back intent triggers navigation

### Phase 4: UI Layer
- T8: Build a generic native horizontal bar chart composable — accepts a generic `List<BarEntry(label: String, value: Float)>`, renders bars descending by value, labels each bar with its label and value. Category-agnostic so it can be reused across the app.
- T9: Build collapsible category row composable — header shows category name + total, expands to show transaction rows (reuse/reference existing transaction row style from MonthlyReportScreen)
- T10: Create `CategorySpendAnalysisScreen` composable — TopBar with "Spend by Category" title and back navigation, bar chart at top (fed with mapped category data), collapsible category list below
