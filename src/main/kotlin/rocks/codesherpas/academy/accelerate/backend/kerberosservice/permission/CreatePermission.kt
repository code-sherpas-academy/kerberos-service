package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission

import org.springframework.stereotype.Component
import java.util.*

@Component
class CreatePermission(val permissionRepository: PermissionRepository) {
    fun execute(description: String): Permission {
        val newPermission = Permission(UUID.randomUUID().toString(), description)
        permissionRepository.save(newPermission)

        return newPermission
    }
}