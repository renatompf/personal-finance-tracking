package io.renatofreire.personalfinancetracking.service

import io.renatofreire.personalfinancetracking.dto.summary.TransactionMonthlySummaryDto
import io.renatofreire.personalfinancetracking.dto.transaction.TransactionInDto
import io.renatofreire.personalfinancetracking.dto.transaction.TransactionOutDto
import io.renatofreire.personalfinancetracking.enums.Category
import io.renatofreire.personalfinancetracking.model.*
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
import java.time.LocalDate
import java.time.ZoneId
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

    fun getMonthlySummary(userDetails: UserDetails, category: Category?): List<TransactionMonthlySummaryDto> {
        val user: User = userRepository.findByEmail(userDetails.username) ?: throw EntityNotFoundException("User not found")
        val monthlyTransactions = if (category == null || category == Category.ALL) {
            transactionMonthlySummaryRepository.findMonthlySummaryByUserId(user.id!!)
        } else {
            transactionMonthlySummaryRepository.findMonthlySummaryByUserIdAndCategory(user.id!!, category)
        }
        val monthlyBudgets = budgetRepository.findAllByUserId(user.id!!)
        return summarizeMonthlyTransactions(monthlyTransactions, monthlyBudgets, category)
    }

    private fun summarizeMonthlyTransactions(
        transactions: List<TransactionMonthlySummary>,
        budgets: List<Budget>,
        category: Category?
    ): List<TransactionMonthlySummaryDto> {

        val budgetMap = budgets.groupBy { budget ->
            LocalDate.ofInstant(budget.budgetDate, ZoneId.of("UTC")).withDayOfMonth(1)
        }.mapValues { budget ->
            budget.value.sumOf { it.limit }
        }

        val summaryMap = transactions.groupBy { transaction ->
            LocalDate.ofInstant(transaction.month, ZoneId.of("UTC")).withDayOfMonth(1)
        }.mapValues { transaction ->
            val totalIncome = transaction.value.sumOf { it.totalIncome ?: BigDecimal.ZERO }
            val totalExpenses = transaction.value.sumOf { it.totalExpenses ?: BigDecimal.ZERO }
            val month = LocalDate.ofInstant(transaction.value.firstOrNull()?.month, ZoneId.of("UTC")).withDayOfMonth(1)
                ?: LocalDate.now()
            val monthlyBudget = budgetMap[month] ?: BigDecimal.ZERO
            val remainingBudget = (monthlyBudget + totalIncome) - (totalExpenses.abs())
            TransactionMonthlySummaryDto(
                month = month.atStartOfDay(ZoneId.of("UTC")).toInstant(),
                category = category ?: Category.ALL,
                totalIncome = totalIncome,
                totalExpenses = totalExpenses,
                remainingBudget = remainingBudget
            )
        }

        return summaryMap.values.toList()
    }
}