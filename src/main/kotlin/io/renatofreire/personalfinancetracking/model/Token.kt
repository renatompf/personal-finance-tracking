package io.renatofreire.personalfinancetracking.model

import io.renatofreire.personalfinancetracking.enums.TokenType
import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "token")
class Token(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    var id: UUID? = null,

    @Column(name = "token", columnDefinition = "text", nullable = false)
    var token: String,

    @Column(name = "type", length = 10, nullable = false)
    @Enumerated(EnumType.STRING)
    var type: TokenType,

    @Column(name = "expired", nullable = false)
    var expired: Boolean,

    @Column(name = "revoked", nullable = false)
    var revoked: Boolean,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    var user: User
) {}