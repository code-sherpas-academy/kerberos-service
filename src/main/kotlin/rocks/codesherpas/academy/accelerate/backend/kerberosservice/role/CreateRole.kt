package rocks.codesherpas.academy.accelerate.backend.kerberosservice.role

import org.springframework.stereotype.Component
import java.util.*

@Component
class CreateRole(val roleRepository: RoleRepository) {
    fun execute(description: String, permissions: List<String>): Role {
        val newRole = Role(UUID.randomUUID().toString(), description, permissions)
        roleRepository.save(newRole)

        return newRole
    }
}