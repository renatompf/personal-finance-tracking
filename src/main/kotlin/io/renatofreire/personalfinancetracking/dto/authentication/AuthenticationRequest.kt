package io.renatofreire.personalfinancetracking.dto.authentication;

import jakarta.validation.constraints.NotNull

data class AuthenticationRequest(
        @field:NotNull val email: String,
        @field:NotNull val password: String
)