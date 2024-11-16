package io.renatofreire.personalfinancetracking.controller

import io.renatofreire.personalfinancetracking.dto.settings.CreatingSettingsDto
import io.renatofreire.personalfinancetracking.dto.settings.SettingsDtoOut
import io.renatofreire.personalfinancetracking.service.SettingsService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/settings")
class SettingsController(
    val settingsService: SettingsService
) {

    @GetMapping
    fun getSettingsByUser(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<SettingsDtoOut> {
        return ResponseEntity.status(HttpStatus.OK).body(settingsService.getSettingsByUser(userDetails))
    }

    @PostMapping
    fun addSettings(@AuthenticationPrincipal userDetails: UserDetails, @RequestBody @Valid request: CreatingSettingsDto): ResponseEntity<SettingsDtoOut> {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(settingsService.createSettings(userDetails, request))
    }

    @PutMapping
    fun updateSettings(@AuthenticationPrincipal userDetails: UserDetails, @RequestBody @Valid request: CreatingSettingsDto): ResponseEntity<SettingsDtoOut> {
        return ResponseEntity.status(HttpStatus.OK).body(settingsService.updateUserSettings(userDetails, request))
    }

}