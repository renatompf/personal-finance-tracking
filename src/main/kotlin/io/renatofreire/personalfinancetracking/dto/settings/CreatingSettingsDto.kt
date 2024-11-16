package io.renatofreire.personalfinancetracking.dto.settings

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.PositiveOrZero
import java.math.BigDecimal

data class CreatingSettingsDto(

    @NotNull(message = "Transaction cannot be null.")
    @NotBlank(message = "Transaction cannot be blank.")
    @JsonProperty(value = "monthlyBudgetValue", required = true)
    @PositiveOrZero(message = "Amount must be positive or zero.")
    val monthlyBudgetValue: BigDecimal

)
