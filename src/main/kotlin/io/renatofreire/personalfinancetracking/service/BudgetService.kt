package io.renatofreire.personalfinancetracking.service

import io.renatofreire.personalfinancetracking.dto.budget.BudgetCreationRequest
import io.renatofreire.personalfinancetracking.dto.budget.BudgetOutDto
import io.renatofreire.personalfinancetracking.dto.budget.RemainingBudgetDto
import io.renatofreire.personalfinancetracking.model.Budget
import io.renatofreire.personalfinancetracking.repository.BudgetRepository
import io.renatofreire.personalfinancetracking.repository.TransactionRepository
import io.renatofreire.personalfinancetracking.repository.UserRepository
import jakarta.persistence.EntityExistsException
import jakarta.persistence.EntityNotFoundException
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class BudgetService(
    private val transactionRepository: TransactionRepository,
    private val userRepository: UserRepository,
    private val budgetRepository: BudgetRepository
) {

    fun addBudget(userDetails: UserDetails, budgetRequest: BudgetCreationRequest): BudgetOutDto {
        val user = userRepository.findByEmail(userDetails.username) ?: throw EntityNotFoundException("User not found")

        val existsByUserIdAndBudgetTime = budgetRepository.existsByUserIdAndBudgetTime(user.id!!)
        if(existsByUserIdAndBudgetTime) {
            throw EntityExistsException("Bidget already set for this month")
        }

        val budget = Budget(
            limit = budgetRequest.limit,
            user = user,
            budgetDate = Instant.now(),
        )

        val save = budgetRepository.save(budget)

        return BudgetOutDto(
            limit = save.limit
        )
    }

    fun calculateRemainingBudget(userDetails: UserDetails): RemainingBudgetDto {
        val user = userRepository.findByEmail(userDetails.username) ?: throw EntityNotFoundException("User not found")

        val monthlyBudget = budgetRepository.findBudgetForCurrentMonth(user.id!!) ?: throw EntityNotFoundException("Budget not found")

        val transactions = transactionRepository.findAllByUserIdForCurrentMonth(user.id!!)

        val totalSpending = transactions.sumOf { it.amount }

        val remainingBudget = monthlyBudget.limit.subtract(totalSpending.abs())

        return RemainingBudgetDto(
            monthlyBudget.limit,
            totalSpending,
            remainingBudget,
        )
    }

}