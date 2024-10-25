package io.renatofreire.personalfinancetracking.controller

import io.renatofreire.personalfinancetracking.dto.authentication.AuthenticationRequest
import io.renatofreire.personalfinancetracking.dto.authentication.AuthenticationResponse
import io.renatofreire.personalfinancetracking.dto.authentication.RegistrationRequest
import io.renatofreire.personalfinancetracking.service.AuthenticationService
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.io.IOException


@RestController
@RequestMapping("/auth")
class AuthenticationController @Autowired constructor(private val authenticationService: AuthenticationService) {
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun registration(@RequestBody registrationRequest: @Valid RegistrationRequest?): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.status(HttpStatus.CREATED).body(authenticationService.register(registrationRequest!!))
    }

    @PostMapping("/login")
    fun login(@RequestBody authenticationRequest: @Valid AuthenticationRequest?): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(
            authenticationService.authenticate(authenticationRequest!!))
    }

    @GetMapping("/refresh-token")
    @Throws(IOException::class)
    fun refreshToken(
        request: HttpServletRequest?,
        response: HttpServletResponse?
    ): ResponseEntity<AuthenticationResponse> {
        return ResponseEntity.status(HttpStatus.OK).body(authenticationService.refreshToken(request!!, response))
    }

}