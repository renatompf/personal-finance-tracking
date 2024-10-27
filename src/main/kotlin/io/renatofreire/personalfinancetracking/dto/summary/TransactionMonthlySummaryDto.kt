package io.renatofreire.personalfinancetracking.dto.summary

import io.renatofreire.personalfinancetracking.enums.Category
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

data class TransactionMonthlySummaryDto(
    val userId: UUID,
    val date: LocalDate,
    val category: Category,
    val totalIncome: BigDecimal,
    val totalExpenses: BigDecimal
)
