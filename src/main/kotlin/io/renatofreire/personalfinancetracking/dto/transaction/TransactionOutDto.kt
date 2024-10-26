package io.renatofreire.personalfinancetracking.dto.transaction

import com.fasterxml.jackson.annotation.JsonProperty
import io.renatofreire.personalfinancetracking.enums.Category
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate

data class TransactionOutDto(
    @NotNull(message = "Transaction cannot be null.")
    @NotBlank(message = "Transaction cannot be blank.")
    @Min(0)
    @JsonProperty(value = "amount", required = true)
    var amount : BigDecimal,

    @NotNull(message = "Transaction cannot be null.")
    @JsonProperty(value = "category", required = true)
    var category : Category,

    @JsonProperty(value = "description", required = false)
    var description : String? = null,

    @JsonProperty(value = "date", required = false)
    var date : LocalDate,
)
