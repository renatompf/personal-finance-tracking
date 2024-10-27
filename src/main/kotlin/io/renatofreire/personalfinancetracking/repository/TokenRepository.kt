package io.renatofreire.personalfinancetracking.repository

import io.renatofreire.personalfinancetracking.model.Token
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TokenRepository : JpaRepository<Token, UUID > {

    fun findByToken(token: String): Token?

    @Query(value = "SELECT t FROM Token t WHERE t.user.id = :userId and (t.expired = false or t.revoked = false)")
    fun findActiveTokensByUserId(userId: UUID): List<Token>
}