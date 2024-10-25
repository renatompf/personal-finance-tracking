package io.renatofreire.personalfinancetracking.configuration

import io.renatofreire.personalfinancetracking.repository.TokenRepository
import io.renatofreire.personalfinancetracking.security.JwtService
import io.renatofreire.personalfinancetracking.security.LogoutService
import io.renatofreire.personalfinancetracking.security.LogoutService.Companion
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JWTAuthenticationFilter (
    private val userDetailsService: UserDetailsService,
    private val jwtService: JwtService,
    private val tokenRepository: TokenRepository
) : OncePerRequestFilter() {
    companion object{
        const val AUTHORIZATION : String = "Authorization"
        const val BEARER : String= "Bearer "
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authenticationHeader = request.getHeader(LogoutService.AUTHORIZATION)

        if (authenticationHeader == null || !authenticationHeader.startsWith(BEARER)) {
            filterChain.doFilter(request, response)
            return
        }

        val jwtToken : String = authenticationHeader.substring(7)

        val userEmail : String? = jwtService.extractUsername(jwtToken)

        if(userEmail != null && SecurityContextHolder.getContext().authentication == null){
            val userDetails : UserDetails = userDetailsService.loadUserByUsername(userEmail)
            val isTokenValid : Boolean = tokenRepository.findByToken(jwtToken)?.let { !it.expired && !it.revoked} ?: false

            if(jwtService.isTokenValid(jwtToken, userDetails) && isTokenValid){
                val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                authToken.details.also {
                    WebAuthenticationDetailsSource().buildDetails(request)
                }
                SecurityContextHolder.getContext().authentication = authToken
            }

        }

        filterChain.doFilter(request, response)

    }


}