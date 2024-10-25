package io.renatofreire.personalfinancetracking.model

import io.renatofreire.personalfinancetracking.enums.Category
import jakarta.persistence.*
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "transaction")
class Transaction (

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    var id : UUID,

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    var amount : BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 50)
    var category : Category,

    @Column(name = "description", columnDefinition = "TEXT")
    var description : String? = null,

    @Column(name = "date", nullable = false)
    var date: Instant = Instant.now(),

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(targetEntity = User::class, fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var user : User

){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Transaction

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}