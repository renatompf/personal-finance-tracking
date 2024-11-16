package io.renatofreire.personalfinancetracking.model;

import jakarta.persistence.*
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "settings")
class Settings (

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    var id:UUID? = null,

    @Column(name = "monthly_budget_value")
    var monthlyBudgetValue: BigDecimal,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    var user: User
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Settings

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

}
