package io.renatofreire.personalfinancetracking.repository

import io.renatofreire.personalfinancetracking.model.Budget
import io.renatofreire.personalfinancetracking.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface BudgetRepository : JpaRepository<Budget, UUID> {

    @Query("select b from Budget b where b.id  = :id")
    fun findAllByUser(user : User): List<Budget>

    @Query("select b from Budget b where b.id  = :id")
    fun findAllByUser(user : User, pageable: Pageable): Page<Budget>

}