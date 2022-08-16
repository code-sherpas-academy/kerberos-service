package rocks.codesherpas.academy.accelerate.backend.kerberosservice

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class PermissionsController(private val permissionRepository: PermissionRepository) {

    @GetMapping("/permissions")
    fun getAll(): List<PermissionResourceWithId> {
        return permissionRepository.findAll().map { permission -> PermissionResourceWithId(permission.id, permission.description) }
    }

    @PostMapping("/permissions")
    fun create(@RequestBody permissionResource: PermissionResource): PermissionResourceWithId {
        val permission = Permission(UUID.randomUUID().toString(), permissionResource.description)
        val savedPermission = permissionRepository.save(permission)

        return PermissionResourceWithId(savedPermission.id, savedPermission.description)
    }
}