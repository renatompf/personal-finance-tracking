package io.renatofreire.personalfinancetracking.repository

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

    @Query("select t from Transaction t where t.user.id = :userId")
    fun findAllByUserId(userId: UUID, pageable: Pageable) : Page<Transaction>

    @Query("select t from Transaction t where t.user.id = :userId order by t.pk.date desc")
    fun findAllByUserId(userId: UUID) : List<Transaction>

    @Query("select t from Transaction t where t.pk.id = :id")
    fun findById(id: UUID): Transaction?

    @Query("""
        select t
        from Transaction t 
        where t.user.id = :userId
        AND t.pk.date >= FUNCTION('date_trunc', 'MONTH', CURRENT_TIMESTAMP) 
        AND b.t.pk.date < FUNCTION('date_trunc', 'MONTH', CURRENT_TIMESTAMP) + INTERVAL '1 month'
        """)
    fun findAllByUserIdForCurrentMonth(userId: UUID): List<Transaction>

    @Query("select t from Transaction t where t.pk.date >= :startDate and t.pk.date <= :endDate order by t.pk.date desc")
    fun findAllBetweenDates(startDate: Instant, endDate: Instant, pageable: Pageable): Page<Transaction>

    @Query("select t from Transaction t where t.pk.date >= :startDate and t.pk.date <= :endDate order by t.pk.date desc")
    fun findAllBetweenDates(startDate: Instant, endDate: Instant): List<Transaction>

}