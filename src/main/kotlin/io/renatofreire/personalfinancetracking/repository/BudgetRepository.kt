package io.renatofreire.personalfinancetracking.repository

import io.renatofreire.personalfinancetracking.model.Budget
import io.renatofreire.personalfinancetracking.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface BudgetRepository : JpaRepository<Budget, UUID> {

    @Query("select b from Budget b where b.user.id  = :userId")
    fun findAllByUserId(userId : UUID): List<Budget>

    @Query("select b from Budget b where b.user.id  = :userId")
    fun findAllByUser(userId : UUID, pageable: Pageable): Page<Budget>

}