package io.renatofreire.personalfinancetracking.model

import jakarta.persistence.*
import java.io.Serializable
import java.time.Instant
import java.util.*

@Embeddable
class TransactionPK(

    @Column(name = "id", nullable = false)
    val id: UUID = UUID.randomUUID(),

    @Column(name = "date", nullable = false)
    var date: Instant = Instant.now()
) : Serializable {
}