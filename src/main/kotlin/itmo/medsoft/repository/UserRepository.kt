package itmo.medsoft.repository

import itmo.medsoft.model.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository : JpaRepository<User, Long> {
    fun findByLogin(login: String): Optional<User>
}