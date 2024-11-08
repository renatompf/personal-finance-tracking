package io.renatofreire.personalfinancetracking.dto.budget

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class BudgetOutDto(
    @NotNull
    @JsonProperty(value = "limit", required = true)
    val limit: BigDecimal
)
