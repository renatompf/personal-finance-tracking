package io.renatofreire.personalfinancetracking.dto.budget

import com.fasterxml.jackson.annotation.JsonProperty
import io.renatofreire.personalfinancetracking.enums.TimePeriod
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class BudgetCreationRequest (

    @NotNull
    @Min(0)
    @JsonProperty(value = "budget", required = true)
    val limit: BigDecimal,

    @NotNull
    @JsonProperty(value = "timePeriod", required = true)
    val timePeriod: TimePeriod,
)
