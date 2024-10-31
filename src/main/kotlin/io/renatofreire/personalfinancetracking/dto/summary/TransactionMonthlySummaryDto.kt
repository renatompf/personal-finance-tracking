package io.renatofreire.personalfinancetracking.dto.summary

import io.renatofreire.personalfinancetracking.enums.Category
import java.math.BigDecimal
import java.time.Instant

data class TransactionMonthlySummaryDto(
    val month: Instant,
    val category: Category,
    val totalIncome: BigDecimal,
    val totalExpenses: BigDecimal,
    val remainingBudget: BigDecimal
)
