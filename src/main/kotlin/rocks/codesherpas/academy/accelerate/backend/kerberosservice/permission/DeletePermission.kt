package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission

import org.springframework.stereotype.Component
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.RoleRepository

@Component
class DeletePermission(
    private val permissionRepository: PermissionRepository,
    private val roleRepository: RoleRepository
) {
    fun execute(id: String) {
        val permissionToBeDeleted = permissionRepository.findById(id)

        if (permissionToBeDeleted != null){
            val roles = roleRepository.findAll()
            roles.forEach{ role ->
                val updatedRole = role.deletePermission(id)
                roleRepository.save(updatedRole)
            }

            permissionRepository.deleteById(id)
        }
    }

}
