package io.renatofreire.personalfinancetracking.repository

import io.renatofreire.personalfinancetracking.enums.Category
import io.renatofreire.personalfinancetracking.model.Budget
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.*

interface BudgetRepository : JpaRepository<Budget, UUID> {

    @Query("select b from Budget b where b.user.id  = :userId")
    fun findAllByUserId(userId : UUID): List<Budget>

    @Query("select b from Budget b where b.user.id  = :userId")
    fun findAllByUser(userId : UUID, pageable: Pageable): Page<Budget>

    @Query("SELECT count(b) > 0 FROM Budget b WHERE b.user.id = :userId AND FUNCTION('date_trunc', 'MONTH', b.budgetDate) = FUNCTION('date_trunc', 'MONTH', NOW())")
    fun existsByUserIdAndBudgetTime(userId: UUID): Boolean

    @Query("""
        SELECT b
        FROM Budget b 
        WHERE b.user.id = :userId 
            AND b.budgetTime >= FUNCTION('date_trunc', 'MONTH', CURRENT_TIMESTAMP) 
            AND b.budgetTime < FUNCTION('date_trunc', 'MONTH', CURRENT_TIMESTAMP) + INTERVAL '1 month'
        """)
    fun findBudgetForCurrentMonth(userId: UUID): Budget?

    @Query("select b from Budget b where b.user.id  = :userId and b.category = :category")
    fun findAllByUserIdAAndCategory(userId : UUID, category: Category): List<Budget>

}