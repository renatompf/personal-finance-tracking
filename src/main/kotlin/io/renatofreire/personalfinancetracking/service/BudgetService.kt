package io.renatofreire.personalfinancetracking.service

import io.renatofreire.personalfinancetracking.dto.budget.BudgetCreationRequest
import io.renatofreire.personalfinancetracking.dto.budget.BudgetOutDto
import io.renatofreire.personalfinancetracking.dto.budget.RemainingBudgetDto
import io.renatofreire.personalfinancetracking.model.Budget
import io.renatofreire.personalfinancetracking.repository.BudgetRepository
import io.renatofreire.personalfinancetracking.repository.TransactionRepository
import io.renatofreire.personalfinancetracking.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class BudgetService(
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository,
    private val budgetRepository: BudgetRepository
) {

    fun addBudget(userDetails: UserDetails, budgetRequest: BudgetCreationRequest): BudgetOutDto {
        val user = userRepository.findByEmail(userDetails.username) ?: throw EntityNotFoundException("User not found")

        var budget = Budget(
            limit = budgetRequest.limit,
            category = budgetRequest.category,
            user = user,
            timePeriod = budgetRequest.timePeriod
        )

        // Save the budget in the database
        budget = budgetRepository.save(budget)

        return BudgetOutDto(
            limit = budget.limit,
            category = budget.category,
        )
    }

    fun calculateRemainingBudget(userDetails: UserDetails): RemainingBudgetDto {
        val user = userRepository.findByEmail(userDetails.username) ?: throw EntityNotFoundException("User not found")

        val monthlyBudgets = budgetRepository.findAllByUserId(user.id!!)

        val totalLimit = monthlyBudgets.sumOf { it.limit }

        val transactions = transactionRepository.findAllByUserId(user.id!!)

        val totalSpending = transactions.sumOf { it.amount }

        val remainingBudget = totalLimit.subtract(totalSpending)

        return RemainingBudgetDto(
            totalLimit,
            totalSpending,
            remainingBudget,
        )
    }

}