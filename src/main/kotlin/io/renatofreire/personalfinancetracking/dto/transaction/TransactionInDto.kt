package io.renatofreire.personalfinancetracking.dto.transaction

import com.fasterxml.jackson.annotation.JsonProperty
import io.renatofreire.personalfinancetracking.enums.Category
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class TransactionInDto(
    @NotNull(message = "Transaction cannot be null.")
    @NotBlank(message = "Transaction cannot be blank.")
    @JsonProperty(value = "amount", required = true)
    var amount : BigDecimal,

    @NotNull(message = "Transaction cannot be null.")
    @JsonProperty(value = "category", required = true)
    var category : Category,

    @JsonProperty(value = "description", required = false)
    var description : String? = null,
)
