package rocks.codesherpas.academy.accelerate.backend.kerberosservice.role

import org.springframework.stereotype.Component

@Component // Annotation should be @Named
class GetRoleById(val roleRepository: RoleRepository) {
    fun execute(id: String): Role? {
        return roleRepository.findById(id)
    }
}