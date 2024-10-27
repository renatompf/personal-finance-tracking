package io.renatofreire.personalfinancetracking.repository

import io.renatofreire.personalfinancetracking.dto.summary.TransactionMonthlySummary
import io.renatofreire.personalfinancetracking.model.Transaction
import io.renatofreire.personalfinancetracking.model.TransactionPK
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.*

@Repository
interface TransactionRepository : JpaRepository<Transaction, TransactionPK> {

    @Query("select t from Transaction t where t.user.id = :userId order by t.pk.date desc")
    fun findAllByUserId(userId: UUID, pageable: Pageable) : Page<Transaction>

    @Query("select t from Transaction t where t.user.id = :userId order by t.pk.date desc")
    fun findAllByUserId(userId: UUID) : List<Transaction>

    @Query("select t from Transaction t where t.pk.id = :id")
    fun findById(id: UUID): Transaction?

    @Query("select t from Transaction t where t.pk.date >= :startDate and t.pk.date <= :endDate order by t.pk.date desc")
    fun findAllBetweenDates(startDate: Instant, endDate: Instant, pageable: Pageable): Page<Transaction>

    @Query("select t from Transaction t where t.pk.date >= :startDate and t.pk.date <= :endDate order by t.pk.date desc")
    fun findAllBetweenDates(startDate: Instant, endDate: Instant): List<Transaction>

    @Query("""
        SELECT new io.renatofreire.personalfinancetracking.dto.summary.TransactionMonthlySummary(
            user_id AS userId,
            to_char(month, 'YYYY-MM') AS date,
            category,
            SUM(total_income) AS totalIncome,
            SUM(total_expenses) AS totalExpenses
        )
        FROM transaction_monthly_summary
        WHERE user_id = :userId
        GROUP BY date, user_id, category
        """)
    fun findMonthlySummaryByUserId(userId: UUID): List<TransactionMonthlySummary>

}