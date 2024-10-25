package io.renatofreire.personalfinancetracking.security

import io.renatofreire.personalfinancetracking.model.Token
import io.renatofreire.personalfinancetracking.repository.TokenRepository
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Service

@Service
class LogoutService(
    private val tokenRepository: TokenRepository
): LogoutHandler {

    companion object{
        const val AUTHORIZATION : String = "Authorization"
        const val BEARER : String= "Bearer "
    }

    override fun logout(request: HttpServletRequest?, response: HttpServletResponse?, authentication: Authentication?) {

        val authenticationHeader = request!!.getHeader(AUTHORIZATION)

        if (authenticationHeader == null || !authenticationHeader.startsWith(BEARER)) {
            return
        }

        val jwtToken = authenticationHeader.substring(7)
        val storedToken: Token = tokenRepository.findByToken(jwtToken)?:return

        storedToken.revoked = true
        storedToken.expired = true
        tokenRepository.save(storedToken)
    }


}