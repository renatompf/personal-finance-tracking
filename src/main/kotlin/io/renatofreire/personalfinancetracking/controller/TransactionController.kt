package io.renatofreire.personalfinancetracking.controller

import io.renatofreire.personalfinancetracking.dto.transaction.TransactionInDto
import io.renatofreire.personalfinancetracking.dto.transaction.TransactionOutDto
import io.renatofreire.personalfinancetracking.service.TransactionService
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

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


}