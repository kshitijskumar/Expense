# Expense Tracker MVP PRD

## 1. Overview
A lightweight personal expense tracker optimized for shared spending.
The app helps users track their own expenses and calculate how much friends owe them,
with final settlement handled externally (e.g., Splitwise).

## 2. Goals
- Fast and simple expense entry
- Clear monthly spending insights
- Easy tracking of shared expenses
- Export-friendly summary for settlement

## 3. Non-Goals
- No settlement handling
- No multi-user sync
- No unequal split logic
- No bank integrations

## 4. Core Features

### 4.1 Expense Management
- Add, edit, delete expense
- Fields:
  - Amount
  - Date (default: today)
  - Category
  - Participants (user + selected friends)
  - Notes (optional)

### 4.2 Categories
- Default categories (Food, Travel, Groceries, etc.)
- Custom categories supported

### 4.3 Friends
- Create and manage friend entities
- Select friends per transaction

### 4.4 Split Logic
- Equal split among selected participants
- User always pays full amount

### 4.5 Budgeting
- Monthly total budget
- Optional category-level budgets
- Remaining budget auto-calculated

### 4.6 Reports
- Monthly total expenses
- Category-wise breakdown
- Budget vs actual

### 4.7 Friend Balances
- Total owed per friend (monthly)
- Drill-down into transactions per friend

### 4.8 Export
- Copy-friendly summary:
  - “Rahul owes ₹1240”
- Optimized for Splitwise entry

### 4.9 Month State
- Mark month as “settled/exported”
- Prevent confusion after export

## 5. UX Principles
- Fast input > feature richness
- Minimal taps to add expense
- Smart defaults (last used category/friends)

## 6. Data Model (High-Level)

### Expense
- id
- amount
- date
- category_id
- notes

### Friend
- id
- name

### ExpenseParticipants
- expense_id
- friend_id (nullable for user)

### Category
- id
- name
- budget_limit (optional)

### MonthlyBudget
- month
- total_limit

### MonthState
- month
- is_settled

## 7. Future Enhancements (Not in MVP)
- Unequal splits
- Multi-device sync
- Payment tracking
- Analytics insights

## 8. Success Metrics
- Daily usage frequency
- Avg time to add expense
- Monthly retention
- Export usage rate
