package io.renatofreire.personalfinancetracking.dto.authentication

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

data class RegistrationRequest(
    @field:NotNull val name: String,
    @field:NotNull @field:Email val email: String,
    @field:NotNull val password: String
)
