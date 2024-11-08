package io.renatofreire.personalfinancetracking.service

import io.renatofreire.personalfinancetracking.dto.summary.TransactionMonthlySummaryDto
import io.renatofreire.personalfinancetracking.dto.transaction.TransactionInDto
import io.renatofreire.personalfinancetracking.dto.transaction.TransactionOutDto
import io.renatofreire.personalfinancetracking.enums.Category
import io.renatofreire.personalfinancetracking.model.Transaction
import io.renatofreire.personalfinancetracking.model.TransactionPK
import io.renatofreire.personalfinancetracking.model.User
import io.renatofreire.personalfinancetracking.repository.BudgetRepository
import io.renatofreire.personalfinancetracking.repository.TransactionMonthlySummaryRepository
import io.renatofreire.personalfinancetracking.repository.TransactionRepository
import io.renatofreire.personalfinancetracking.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.*

@Service
class TransactionService(
    private val userRepository: UserRepository,
    private val transactionRepository: TransactionRepository,
    private val transactionMonthlySummaryRepository: TransactionMonthlySummaryRepository,
    private val budgetRepository: BudgetRepository
){

    fun getAllTransactions(userDetails: UserDetails, pageable: Pageable): Page<TransactionOutDto> {

        val user : User = userRepository.findByEmail(userDetails.username) ?: return Page.empty()

        return transactionRepository.findAllByUserId(user.id!!, pageable).map { transaction ->
            TransactionOutDto(
                id = transaction.pk.id,
                amount = transaction.amount,
                category = transaction.category,
                description = transaction.description,
                date = LocalDate.ofInstant(transaction.pk.date, ZoneId.of("UTC"))
            )
        }

    }

    fun getTransactionById(userDetails: UserDetails, transactionId: UUID): TransactionOutDto {

        val user : User = userRepository.findByEmail(userDetails.username) ?: throw EntityNotFoundException("User not found")

        val transaction : Transaction = transactionRepository.findById(transactionId)
            ?: throw EntityNotFoundException("Transaction with id $transactionId not found")

        if(!transaction.user.equals(user)) {
            throw EntityNotFoundException("User with id $transactionId not found")
        }

        return TransactionOutDto(
            id = transaction.pk.id,
            amount = transaction.amount,
            category = transaction.category,
            description = transaction.description,
            date = LocalDate.ofInstant(transaction.pk.date, ZoneId.of("UTC"))
        )

    }

    fun saveTransaction(userDetails: UserDetails, request: TransactionInDto) : TransactionOutDto {
        val user : User = userRepository.findByEmail(userDetails.username) ?: throw EntityNotFoundException("User not found")

        val transaction = transactionRepository.save(
            Transaction(
                TransactionPK(),
                amount = request.amount,
                category = request.category,
                description = request.description,
                user
            )
        )

        return TransactionOutDto(
            id = transaction.pk.id,
            amount = transaction.amount,
            category = transaction.category,
            description = transaction.description,
            date = LocalDate.ofInstant(transaction.pk.date, ZoneId.of("UTC"))
        )

    }

    fun deleteTransaction(userDetails: UserDetails, transactionId: UUID) {
        val user : User = userRepository.findByEmail(userDetails.username) ?: throw EntityNotFoundException("User not found")

        val transaction : Transaction = transactionRepository.findById(transactionId) ?: throw EntityNotFoundException("Transaction with id $transactionId not found")

        if(!transaction.user.equals(user)) {
            throw EntityNotFoundException("User with id $transactionId not found")
        }

        transactionRepository.delete(transaction)
    }

    fun getMonthlySummary(userDetails: UserDetails): List<TransactionMonthlySummaryDto> {
        val user: User = userRepository.findByEmail(userDetails.username)
            ?: throw EntityNotFoundException("User not found")

        val monthlyTransactions = transactionMonthlySummaryRepository.findMonthlySummaryByUserId(user.id!!)

        val monthlyBudgets = budgetRepository.findAllByUserId(user.id!!)

        // (Limit + monthlyTransactions.getTotalIncome - monthlyTransactions.getTotalExpenses) for each month
        val budgetMap = monthlyBudgets.groupBy { budget ->

            LocalDate.ofInstant(budget.budgetDate, ZoneId.of("UTC")).withDayOfMonth(1)
        }.mapValues { budgets ->
            // Sum of limits for the month
            budgets.value.sumOf { it.limit }
        }

        val summaryMap = monthlyTransactions.groupBy { transaction ->

            LocalDate.ofInstant(transaction.month, ZoneId.of("UTC")).withDayOfMonth(1)
        }.mapValues { transactions ->
            val totalIncome = transactions.value.sumOf { it.totalIncome ?: BigDecimal.ZERO }
            val totalExpenses = transactions.value.sumOf { it.totalExpenses ?: BigDecimal.ZERO }

            // Get the month from the first transaction in this group (using LocalDate to compare)
            val month = LocalDate.ofInstant(transactions.value.firstOrNull()?.month, ZoneId.of("UTC")).withDayOfMonth(1)
                ?: LocalDate.now() // Fallback to current month if no transactions

            // Get the budget limit for the month (default to 0 if none found)
            val monthlyBudget = budgetMap[month] ?: BigDecimal.ZERO

            // Calculate remaining budget
            val remainingBudget = monthlyBudget + totalIncome - totalExpenses

            // Return DTO
            TransactionMonthlySummaryDto(
                month = month.atStartOfDay(ZoneId.of("UTC")).toInstant(),
                category = Category.ALL,
                totalIncome = totalIncome,
                totalExpenses = totalExpenses,
                remainingBudget = remainingBudget
            )
        }

        return summaryMap.values.toList()
    }


    fun getMonthlySummaryByCategory(userDetails: UserDetails, category: Category): List<TransactionMonthlySummaryDto> {
        val user: User = userRepository.findByEmail(userDetails.username)
            ?: throw EntityNotFoundException("User not found")

        // Retrieve monthly transactions
        val transactionsByCategory = transactionMonthlySummaryRepository.findMonthlySummaryByUserIdAndCategory(user.id!!, category)

        val budgetsByCategory = budgetRepository.findAllByUserId(user.id!!)
            .groupBy { budget ->
                budget.budgetDate.truncatedTo(ChronoUnit.MONTHS)
            }.mapValues { budgets -> budgets.value.sumOf { it.limit } // Sum of limits for the month
        }

        val summaryMap = transactionsByCategory.groupBy { transaction ->
            transaction.month?.truncatedTo(ChronoUnit.MONTHS) // Group by month
        }.mapValues { transactions ->
            val totalIncome = transactions.value.sumOf { it.totalIncome ?: BigDecimal.ZERO }
            val totalExpenses = transactions.value.sumOf { it.totalExpenses ?: BigDecimal.ZERO }

            // Get the month from the first transaction in this group
            val month = transactions.value.firstOrNull()?.month ?: Instant.now()

            // Get the budget limit for the month (default to 0 if none found)
            val monthlyBudget = budgetsByCategory[month] ?: BigDecimal.ZERO

            // Calculate remaining budget
            val remainingBudget = monthlyBudget + totalIncome - totalExpenses

            TransactionMonthlySummaryDto(
                month = month,
                category = category,
                totalIncome = totalIncome,
                totalExpenses = totalExpenses,
                remainingBudget = remainingBudget
            )
        }

        // Return a list of summary DTOs
        return summaryMap.values.toList()
    }

}