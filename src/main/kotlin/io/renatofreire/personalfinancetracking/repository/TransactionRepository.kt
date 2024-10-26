package io.renatofreire.personalfinancetracking.repository

import io.renatofreire.personalfinancetracking.model.Transaction
import io.renatofreire.personalfinancetracking.model.TransactionPK
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.Instant
import java.time.LocalDate
import java.util.UUID

interface TransactionRepository : JpaRepository<Transaction, TransactionPK> {

    @Query("select t from Transaction t where t.user.id = :userId order by t.pk.date desc")
    fun findAllByUserId(userId: UUID, pageable: Pageable) : Page<Transaction>

    @Query("select t from Transaction t where t.pk.id = :id")
    fun findById(id: UUID): Transaction?

    @Query("select t from Transaction t where t.pk.date >= :startDate and t.pk.date <= :endDate order by t.pk.date desc")
    fun findAllBetweenDates(startDate: Instant, endDate: Instant, pageable: Pageable): Page<Transaction>



}