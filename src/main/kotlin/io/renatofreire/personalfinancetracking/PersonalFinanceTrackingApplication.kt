package io.renatofreire.personalfinancetracking

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class PersonalFinanceTrackingApplication

fun main(args: Array<String>) {
    runApplication<PersonalFinanceTrackingApplication>(*args)
}
