package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission

import org.springframework.stereotype.Component

@Component // Annotation should be @Named
class GetPermissionById(val permissionRepository: PermissionRepository) {
    fun execute(id: String): Permission? {
        return permissionRepository.findById(id)
    }
}