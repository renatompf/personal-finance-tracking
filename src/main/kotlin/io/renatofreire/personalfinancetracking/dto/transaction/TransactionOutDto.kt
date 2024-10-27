package io.renatofreire.personalfinancetracking.dto.transaction

import com.fasterxml.jackson.annotation.JsonProperty
import io.renatofreire.personalfinancetracking.enums.Category
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*

data class TransactionOutDto(

    @NotNull(message = "ID cannot be null.")
    var id: UUID,

    @NotNull(message = "Amount cannot be null.")
    @NotBlank(message = "Amount cannot be blank.")
    @JsonProperty(value = "amount", required = true)
    var amount : BigDecimal,

    @NotNull(message = "Category cannot be null.")
    @JsonProperty(value = "category", required = true)
    var category : Category,

    @JsonProperty(value = "description", required = false)
    var description : String? = null,

    @JsonProperty(value = "date", required = false)
    var date : LocalDate,
)
