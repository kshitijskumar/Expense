This contains a rough PRD for view transaction screen

## Requirement
1. This screen should suffice the monthly expenditure analysis and display usecase
2. User should be able to change month on this screen for a month base analysis
3. This screen should provide following information for a month
   1. Total money spent
   2. Top 3 or less categories where most amount of money was spent and an option to "View all categories"
   3. Top 3 or less friends where most amount of money was split across and an option to "View all friends"
4. List of transactions of the month separated by date

## High level plan of action
1. create View transactions screen args and navigator setup
2. work on monthExpenseAnalysisManager - responsible for fulfilling all the datapoints we require for showing in the screen
3. vm implementation
4. screen implementaion


## Rough UI vision
```
┌─────────────────────────────────────────┐
│ ◄ Transactions       < March > [Today]  │
└─────────────────────────────────────────┘

┌─────────────────────────────────────────┐
│                                         │
│      Total spent: Rs. 25000            │
│                                         │
└─────────────────────────────────────────┘

┌──────────────────┐  ┌──────────────────┐
│ CATEGORIES       │  │ FRIENDS          │
│ ─────────────────│  │ ─────────────────│
│ Groceries        │  │ Friend1          │
│ Rs. 100          │  │ Rs. 500          │
│                  │  │                  │
│ Utilities        │  │ Friend2          │
│ Rs. 200          │  │ Rs. 100          │
│                  │  │                  │
│ Entertainment    │  │ Family           │
│ Rs. 500          │  │ Rs. 300          │
│                  │  │                  │
│ + view all       │  │ + view all       │
└──────────────────┘  └──────────────────┘

┌─────────────────────────────────────────┐
│ TODAY                                   │
├─────────────────────────────────────────┤
│ ┌─────────────────────────────────────┐ │
│ │ Coffee Shop         Rs. 150         │ │
│ │ 10:30 AM                            │ │
│ └─────────────────────────────────────┘ │
│                                         │
│ ┌─────────────────────────────────────┐ │
│ │ Pharmacy            Rs. 300         │ │
│ │ 2:45 PM                             │ │
│ └─────────────────────────────────────┘ │
│                                         │
├─────────────────────────────────────────┤
│ YESTERDAY                               │
├─────────────────────────────────────────┤
│ ┌─────────────────────────────────────┐ │
│ │ Grocery Store       Rs. 1200        │ │
│ │ 9:15 AM                             │ │
│ └─────────────────────────────────────┘ │
│                                         │
│ ┌─────────────────────────────────────┐ │
│ │ Fuel                Rs. 800         │ │
│ │ 5:30 PM                             │ │
│ └─────────────────────────────────────┘ │
│                                         │
├─────────────────────────────────────────┤
│ 24 MARCH                                │
├─────────────────────────────────────────┤
│ ┌─────────────────────────────────────┐ │
│ │ Restaurant          Rs. 2500        │ │
│ │ 8:00 PM                             │ │
│ └─────────────────────────────────────┘ │
└─────────────────────────────────────────┘
for transactions card, use the same card present in HomeScreen instead of creating new one
```