package rocks.codesherpas.academy.accelerate.backend.kerberosservice.role

import org.springframework.stereotype.Component

@Component
class UpdateRole(val roleRepository: RoleRepository) {
    fun execute(id: String, newDescription: String): Role {
        val role = roleRepository.findById(id)

        if (role != null) {
            val updatedRole = Role(id, newDescription, role.permissions)
            roleRepository.save(updatedRole)
            return updatedRole
        } else {
            throw Exception("No role with id: $id")
        }
    }
}