package io.renatofreire.personalfinancetracking.configuration

import io.renatofreire.personalfinancetracking.repository.UserRepository
import io.renatofreire.personalfinancetracking.security.CustomUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder

@Configuration
class ApplicationConfiguration(
    private val userRepository: UserRepository
) {

    @Bean
    fun PasswordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun userDetailsService(): UserDetailsService = CustomUserDetailsService(userRepository)

    @Bean
    fun authenticationProvider() : AuthenticationProvider{
        return DaoAuthenticationProvider()
            .also {
                it.setUserDetailsService(userDetailsService())
                it.setPasswordEncoder(PasswordEncoder())
            }
    }

    @Bean
    fun authenticationManager(authenticationConfiguration: AuthenticationConfiguration): AuthenticationManager = authenticationConfiguration.authenticationManager

}