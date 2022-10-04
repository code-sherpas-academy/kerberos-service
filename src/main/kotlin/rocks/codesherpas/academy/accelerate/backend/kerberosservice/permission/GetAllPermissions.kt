package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission

import org.springframework.stereotype.Component

@Component
class GetAllPermissions(private val permissionRepository: PermissionRepository) {
    fun execute(): List<Permission> {
        return permissionRepository.findAll()
    }

}
