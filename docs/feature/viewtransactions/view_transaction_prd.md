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