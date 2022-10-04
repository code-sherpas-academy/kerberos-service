package rocks.codesherpas.academy.accelerate.backend.kerberosservice.role

import org.springframework.stereotype.Component

@Component
class DeleteRole(val roleRepository: RoleRepository) {
    fun execute(id: String) {
        roleRepository.delete(id)
    }
}