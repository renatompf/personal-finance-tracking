package io.renatofreire.personalfinancetracking.repository

import io.renatofreire.personalfinancetracking.enums.Category
import io.renatofreire.personalfinancetracking.model.TransactionMonthlySummary
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface TransactionMonthlySummaryRepository : JpaRepository<TransactionMonthlySummary, UUID> {

    @Query(
        """
        SELECT t
        FROM TransactionMonthlySummary t
        WHERE t.userId = :userId
        GROUP BY t.month, t.userId, t.category, t.totalExpenses, t.totalIncome
        """
    )
    fun findMonthlySummaryByUserId(@Param("userId") userId: UUID): List<TransactionMonthlySummary>

    @Query(
        """
        SELECT t
        FROM TransactionMonthlySummary t
        WHERE t.userId = :userId
        AND t.category = :category
        GROUP BY t.month, t.userId, t.category, t.totalExpenses, t.totalIncome
        """
    )
    fun findMonthlySummaryByUserIdAndCategory(userId: UUID, category: Category): List<TransactionMonthlySummary>
}