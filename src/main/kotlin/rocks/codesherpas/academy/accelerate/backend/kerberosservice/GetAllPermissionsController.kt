package rocks.codesherpas.academy.accelerate.backend.kerberosservice

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class GetAllPermissionsController(private val permissionRepository: PermissionRepository) {

    @GetMapping("/permissions")
    fun getAll(): List<PermissionResource> {
        return permissionRepository.findAll().map { permission -> PermissionResource(permission.id, permission.description) }
    }
}