package org.example.project.feature.monthlyreport.domain.model

import org.example.project.domain.model.FriendModel

/**
 * Amount owed by a friend for their share of expenses in a month.
 *
 * @param friend The friend details (id, name)
 * @param amountOwed Total amount this friend owes for the month.
 *                   Calculated as: sum of (expense.amount / (participantCount + 1)) for each expense
 *                   The +1 accounts for the user who always participates.
 */
data class FriendSpend(
    val friend: FriendModel,
    val amountOwed: Long
)
