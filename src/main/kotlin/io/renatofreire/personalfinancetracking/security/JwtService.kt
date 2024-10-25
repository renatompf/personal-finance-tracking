package io.renatofreire.personalfinancetracking.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService (
    @Value("\${application.security.jwt.secret-key}")
    private val secretKey: String,

    @Value("\${application.security.jwt.expiration}")
    private val jwtTokenExpiration: Long,

    @Value("\${application.security.jwt.refreshToken.expiration}")
    private val refreshTokenExpiration : Long
){

    private fun getSignInKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }

    private fun extractAllClaims(token: String): Claims {
        return Jwts.parser()
            .verifyWith(getSignInKey())
            .build()
            .parseSignedClaims(token)
            .payload
    }

    private fun <T> extractClaim(token: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(token)
        return claimsResolver(claims)
    }

    fun extractUsername(jwtToken: String): String? {
        return extractClaim(jwtToken) { claims -> claims.subject }
    }

    private fun extractExpiration(token: String): Date {
        return extractClaim(token) { claims -> claims.expiration }
    }

    private fun isTokenExpired(token: String): Boolean {
        return extractExpiration(token).before(Date())
    }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val username = extractUsername(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    fun generateJWTToken(userDetails: UserDetails): String {
        return generateJWTToken(emptyMap(), userDetails)
    }

    fun generateJWTToken(extractClaims: Map<String, Any>, userDetails: UserDetails): String {
        return buildToken(extractClaims, userDetails, jwtTokenExpiration)
    }

    fun generateRefreshToken(userDetails: UserDetails): String {
        return buildToken(emptyMap(), userDetails, refreshTokenExpiration)
    }

    private fun buildToken(extractClaims: Map<String, Any>, userDetails: UserDetails, expiration: Long): String {
        return Jwts.builder()
            .claims(extractClaims)
            .subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey(), Jwts.SIG.HS256)
            .compact()
    }

}