package io.renatofreire.personalfinancetracking.dto.authentication

import jakarta.validation.constraints.NotNull

data class AuthenticationResponse(
    @field:NotNull val token: String,
    @field:NotNull val refreshToken: String
)
