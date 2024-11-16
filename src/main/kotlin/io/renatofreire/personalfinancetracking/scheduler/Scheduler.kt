package io.renatofreire.personalfinancetracking.scheduler

import io.renatofreire.personalfinancetracking.model.Budget
import io.renatofreire.personalfinancetracking.repository.BudgetRepository
import io.renatofreire.personalfinancetracking.repository.SettingsRepository
import io.renatofreire.personalfinancetracking.repository.UserRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class Scheduler(
    private val budgetRepository: BudgetRepository,
    private val settingsRepository: SettingsRepository,
    private val userRepository: UserRepository
) {

    @Scheduled(cron = "0 0 0 1 * ?")
    fun createsBudgetsForUsers(){
        val users = userRepository.findAll()
        val settings = settingsRepository.findAll()

        users.forEach { user ->
            val userSettings = settings.find { it.user.id == user.id } ?: return

            val budget = Budget(
                limit = userSettings.monthlyBudgetValue,
                user = user,
                budgetDate = Instant.now(),
            )

            budgetRepository.save(budget)
        }

    }

}