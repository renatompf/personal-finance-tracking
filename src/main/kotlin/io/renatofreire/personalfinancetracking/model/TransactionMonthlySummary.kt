package io.renatofreire.personalfinancetracking.model

import io.renatofreire.personalfinancetracking.enums.Category
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.util.*


@Entity
@Table(name = "transaction_monthly_summary")
class TransactionMonthlySummary {

    @Id
    @Column(name = "user_id")
    val userId: UUID? = null

    @Column(name = "month")
    val month: Instant? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    val category: Category? = null

    @Column(name = "total_income")
    val totalIncome: BigDecimal? = null

    @Column(name = "total_expenses")
    val totalExpenses: BigDecimal? = null

}