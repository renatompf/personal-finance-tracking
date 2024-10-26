package io.renatofreire.personalfinancetracking.dto.summary

import java.math.BigDecimal
import java.util.*

data class TransactionMonthlySummary(
    val userId: UUID,
    val date: String,
    val category: String,
    val totalIncome: BigDecimal,
    val totalExpenses: BigDecimal
)
