package io.renatofreire.personalfinancetracking.repository

import io.renatofreire.personalfinancetracking.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : JpaRepository<User, UUID> {

    fun findByEmail(email: String): User?

    fun existsByEmail(email: String): Boolean

}