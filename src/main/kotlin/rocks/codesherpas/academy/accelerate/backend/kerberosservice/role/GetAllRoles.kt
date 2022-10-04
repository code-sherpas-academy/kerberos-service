package rocks.codesherpas.academy.accelerate.backend.kerberosservice.role

import org.springframework.stereotype.Component

@Component // This should be @Named
class GetAllRoles(val roleRepository: RoleRepository) {
    fun execute(): List<Role> {
        return roleRepository.findAll()
    }
}