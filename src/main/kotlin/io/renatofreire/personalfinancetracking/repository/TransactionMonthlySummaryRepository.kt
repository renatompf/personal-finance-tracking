package io.renatofreire.personalfinancetracking.repository

import io.renatofreire.personalfinancetracking.dto.summary.TransactionMonthlySummary
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TransactionMonthlySummaryRepository {

    @Query("""
        SELECT new io.renatofreire.personalfinancetracking.dto.summary.TransactionMonthlySummary(
            to_char(month, 'YYYY-MM') AS date,
            user_id AS userId,
            category,
            SUM(total_income) AS totalIncome,
            SUM(total_expenses) AS totalExpenses
        )
        FROM transaction_monthly_summary
        WHERE user_id = :userId
        GROUP BY date, user_id, category
        """)
    fun findMonthlySummaryByUserId(@Param("userId") userId: UUID): List<TransactionMonthlySummary>
}