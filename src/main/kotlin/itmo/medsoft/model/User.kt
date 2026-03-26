package itmo.medsoft.model

import itmo.medsoft.model.enum.HashAlgorithm
import jakarta.persistence.*
import java.awt.Stroke
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(unique = true, nullable = false)
    val login: String,

    @Column(nullable = false)
    val passwordHash: String,

    @Column(nullable = false)
    val algorithm: String,

    val createdAt: LocalDateTime = LocalDateTime.now()
)