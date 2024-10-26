package io.renatofreire.personalfinancetracking.dto.budget

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class RemainingBudgetDto (
    @NotNull
    @JsonProperty(value = "totalLimit", required = true)
    val totalLimit: BigDecimal,

    @NotNull
    @JsonProperty(value = "totalSpending", required = true)
    val totalSpending: BigDecimal,

    @NotNull
    @JsonProperty(value = "remainingBudget", required = true)
    val remainingBudget: BigDecimal

)
