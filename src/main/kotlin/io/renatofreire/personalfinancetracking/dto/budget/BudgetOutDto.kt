package io.renatofreire.personalfinancetracking.dto.budget

import com.fasterxml.jackson.annotation.JsonProperty
import io.renatofreire.personalfinancetracking.enums.Category
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class BudgetOutDto(
    @NotNull
    @JsonProperty(value = "limit", required = true)
    val limit: BigDecimal,
    @NotNull
    @JsonProperty(value = "category", required = true)
    val category: Category,
)
