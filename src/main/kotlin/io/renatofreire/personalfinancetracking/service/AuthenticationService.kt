package io.renatofreire.personalfinancetracking.service

import io.renatofreire.personalfinancetracking.dto.authentication.AuthenticationRequest
import io.renatofreire.personalfinancetracking.dto.authentication.AuthenticationResponse
import io.renatofreire.personalfinancetracking.dto.authentication.RegistrationRequest
import io.renatofreire.personalfinancetracking.enums.Role
import io.renatofreire.personalfinancetracking.enums.TokenType
import io.renatofreire.personalfinancetracking.model.Token
import io.renatofreire.personalfinancetracking.model.User
import io.renatofreire.personalfinancetracking.repository.TokenRepository
import io.renatofreire.personalfinancetracking.repository.UserRepository
import io.renatofreire.personalfinancetracking.security.JwtService
import jakarta.persistence.EntityExistsException
import jakarta.persistence.EntityNotFoundException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.io.IOException
import java.security.InvalidParameterException
import java.util.function.Consumer


@Service
class AuthenticationService(
    private val userRepository: UserRepository,
    private val tokenRepository: TokenRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
) {

    fun register(registrationRequest: RegistrationRequest): AuthenticationResponse {
        if(userRepository.existsByEmail(registrationRequest.email)) {
            throw EntityExistsException("Email already taken")
        }

        val newUser: User = User(
            name = registrationRequest.name,
            email = registrationRequest.email,
            hashedPassword = passwordEncoder.encode(registrationRequest.password),
            role = Role.USER
        )

        val jwtToken = saveJWTToken(newUser)
        val refreshToken = jwtService.generateRefreshToken(newUser)
        return AuthenticationResponse(jwtToken, refreshToken)
    }

    fun authenticate(authenticateRequest: AuthenticationRequest): AuthenticationResponse {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                authenticateRequest.email,
                authenticateRequest.password
            )
        )

        val user: User = userRepository.findByEmail(authenticateRequest.email) ?: throw EntityNotFoundException("User not found")

        revokeAllUserTokens(user)
        val jwtToken = saveJWTToken(user)
        val refreshToken = jwtService.generateRefreshToken(user)

        return AuthenticationResponse(jwtToken, refreshToken)
    }

    @Throws(IOException::class)
    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse?): AuthenticationResponse {
        val authenticationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
            throw InvalidParameterException()
        }

        val refreshToken = authenticationHeader.substring(7)

        val userEmail = jwtService.extractUsername(refreshToken) ?: throw InvalidParameterException()

        val user: User = userRepository.findByEmail(userEmail) ?: throw EntityNotFoundException()

        if (!jwtService.isTokenValid(refreshToken, user)) {
            throw InvalidParameterException()
        }

        revokeAllUserTokens(user)
        val accessToken = saveJWTToken(user)

        return AuthenticationResponse(accessToken, refreshToken)
    }

    private fun saveJWTToken(newUser: User): String {
        val savedUser: User = userRepository.save(newUser)

        val jwtToken = jwtService.generateJWTToken(newUser)

        val token: Token = Token(
            token = jwtToken,
            type = TokenType.BEARER,
            expired = false,
            revoked = false,
            user = savedUser)

        tokenRepository.save(token)
        return jwtToken
    }

    private fun revokeAllUserTokens(newUser: User) {
        val allValidTokens: List<Token> = tokenRepository.findActiveTokensByUserId(newUser.id!!)
        if (allValidTokens.isEmpty()) {
            return
        }

        allValidTokens.forEach(Consumer { token: Token ->
            token.expired = true
            token.revoked = true
        })

        tokenRepository.saveAll(allValidTokens)
    }
}