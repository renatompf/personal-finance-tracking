package io.renatofreire.personalfinancetracking

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PersonalFinanceTrackingApplication

fun main(args: Array<String>) {
    runApplication<PersonalFinanceTrackingApplication>(*args)
}
