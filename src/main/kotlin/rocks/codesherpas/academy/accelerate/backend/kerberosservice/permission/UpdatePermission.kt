package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission

import org.springframework.stereotype.Component

@Component
class UpdatePermission(
    private val permissionRepository: PermissionRepository,
) {
    fun execute(id: String, newDescription: String): Permission {
        val premission = permissionRepository.findById(id)

        if (premission != null) {
            val updatedPermission = Permission(id, newDescription)
            permissionRepository.save(updatedPermission)
            return updatedPermission
        } else {
            throw Exception("No permission with id: $id")
        }
    }

}