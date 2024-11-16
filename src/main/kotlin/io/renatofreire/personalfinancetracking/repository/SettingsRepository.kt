package io.renatofreire.personalfinancetracking.repository

import io.renatofreire.personalfinancetracking.model.Settings
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface SettingsRepository: JpaRepository<Settings, UUID> {

    fun findByUserId(userId: UUID): Settings?

}