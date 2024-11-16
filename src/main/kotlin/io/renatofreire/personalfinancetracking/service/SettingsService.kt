package io.renatofreire.personalfinancetracking.service

import io.renatofreire.personalfinancetracking.dto.settings.CreatingSettingsDto
import io.renatofreire.personalfinancetracking.dto.settings.SettingsDtoOut
import io.renatofreire.personalfinancetracking.model.Settings
import io.renatofreire.personalfinancetracking.repository.SettingsRepository
import io.renatofreire.personalfinancetracking.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service

@Service
class SettingsService (
    private val userRepository: UserRepository,
    private val settingsRepository: SettingsRepository
) {

    fun createSettings(userDetails: UserDetails, dto: CreatingSettingsDto): SettingsDtoOut {
        val settings = Settings(
            monthlyBudgetValue = dto.monthlyBudgetValue,
            user = userRepository.findByEmail(userDetails.username) ?: throw EntityNotFoundException("User not found")
        )

        val savedSettings = settingsRepository.save(settings)
        return SettingsDtoOut(savedSettings.monthlyBudgetValue)
    }

    fun getSettingsByUser(userDetails: UserDetails): SettingsDtoOut? {
        val user = userRepository.findByEmail(userDetails.username) ?: throw EntityNotFoundException("User not found")
        val settings = settingsRepository.findByUserId(user.id!!) ?: throw EntityNotFoundException("Settings not found")

        return SettingsDtoOut(settings.monthlyBudgetValue)
    }

    fun updateUserSettings(userDetails: UserDetails, dto: CreatingSettingsDto): SettingsDtoOut? {
        val user = userRepository.findByEmail(userDetails.username) ?: throw EntityNotFoundException("User not found")
        val settings = settingsRepository.findByUserId(user.id!!) ?: throw EntityNotFoundException("Settings not found")

        settings.monthlyBudgetValue = dto.monthlyBudgetValue
        val updatedSettings = settingsRepository.save(settings)

        return SettingsDtoOut(updatedSettings.monthlyBudgetValue)
    }

}
