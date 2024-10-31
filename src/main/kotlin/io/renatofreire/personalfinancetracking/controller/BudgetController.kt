package io.renatofreire.personalfinancetracking.controller

import io.renatofreire.personalfinancetracking.dto.budget.BudgetCreationRequest
import io.renatofreire.personalfinancetracking.dto.budget.BudgetOutDto
import io.renatofreire.personalfinancetracking.dto.budget.RemainingBudgetDto
import io.renatofreire.personalfinancetracking.service.BudgetService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("budgets")
class BudgetController(
    private val budgetService: BudgetService
){

    @PostMapping
    fun addBudget(@AuthenticationPrincipal userDetails: UserDetails, @RequestBody @Valid budgetRequest: BudgetCreationRequest): ResponseEntity<BudgetOutDto> {
        return ResponseEntity.status(HttpStatus.OK).body(budgetService.addBudget(userDetails, budgetRequest))
    }

    @GetMapping("/remaining")
    fun getRemainingBudget(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<RemainingBudgetDto> {
        return ResponseEntity.status(HttpStatus.OK).body(budgetService.calculateRemainingBudget(userDetails))
    }

}