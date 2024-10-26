package io.renatofreire.personalfinancetracking.service

import io.renatofreire.personalfinancetracking.dto.transaction.TransactionInDto
import io.renatofreire.personalfinancetracking.dto.transaction.TransactionOutDto
import io.renatofreire.personalfinancetracking.model.Transaction
import io.renatofreire.personalfinancetracking.model.TransactionPK
import io.renatofreire.personalfinancetracking.model.User
import io.renatofreire.personalfinancetracking.repository.TransactionRepository
import io.renatofreire.personalfinancetracking.repository.UserRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID

@Service
class TransactionService(
    private val userRepository: UserRepository,
    private val transactionRepository: TransactionRepository
){

    fun getAllTransactions(userDetails: UserDetails, pageable: Pageable): Page<TransactionOutDto> {

        val user : User = userRepository.findByEmail(userDetails.username) ?: return Page.empty()

        return transactionRepository.findAllByUserId(user.id!!, pageable).map { transaction ->
            TransactionOutDto(
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

}