package io.renatofreire.personalfinancetracking.controller

import io.renatofreire.personalfinancetracking.dto.summary.TransactionMonthlySummaryDto
import io.renatofreire.personalfinancetracking.dto.transaction.TransactionInDto
import io.renatofreire.personalfinancetracking.dto.transaction.TransactionOutDto
import io.renatofreire.personalfinancetracking.enums.Category
import io.renatofreire.personalfinancetracking.service.TransactionService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/transactions")
class TransactionController(
    val transactionService: TransactionService
) {

    @GetMapping
    fun getAllTransactions(@AuthenticationPrincipal userDetails: UserDetails, pageable: Pageable): ResponseEntity<Page<TransactionOutDto>> {
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getAllTransactions(userDetails, pageable))
    }

    @GetMapping("/{transactionId}")
    fun getAllTransactions(@AuthenticationPrincipal userDetails: UserDetails, @PathVariable transactionId: UUID): ResponseEntity<TransactionOutDto> {
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getTransactionById(userDetails, transactionId))
    }

    @PostMapping
    fun addTransaction(@AuthenticationPrincipal userDetails: UserDetails, @RequestBody @Valid request: TransactionInDto): ResponseEntity<TransactionOutDto> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(transactionService.saveTransaction(userDetails, request))
    }

    @DeleteMapping("/{transactionId}")
    fun deleteTransaction(@AuthenticationPrincipal userDetails: UserDetails, @PathVariable transactionId: UUID): ResponseEntity<Void> {
        transactionService.deleteTransaction(userDetails, transactionId)
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build()
    }

    @GetMapping("/summary/monthly")
    fun getMonthlySummary(@AuthenticationPrincipal userDetails: UserDetails, @RequestParam(name = "category", required = false) category: Category?): ResponseEntity<List<TransactionMonthlySummaryDto>> {
        return ResponseEntity.status(HttpStatus.OK).body(transactionService.getMonthlySummary(userDetails, category))
    }

}