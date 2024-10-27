package io.renatofreire.personalfinancetracking.model

import io.renatofreire.personalfinancetracking.enums.Category
import io.renatofreire.personalfinancetracking.enums.TimePeriod
import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "budget")
class Budget (

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    var id: UUID? = null,

    @Column(name = "\"limit\"", nullable = false, precision = 10, scale = 2)
    var limit: BigDecimal,

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 50)
    var category: Category,

    @Enumerated(EnumType.STRING)
    @Column(name = "time_period", nullable = false, length = 50)
    var timePeriod: TimePeriod, // Enum for MONTHLY or ANNUALLY

    @ManyToOne(targetEntity = User::class, cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User
){

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Budget

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}