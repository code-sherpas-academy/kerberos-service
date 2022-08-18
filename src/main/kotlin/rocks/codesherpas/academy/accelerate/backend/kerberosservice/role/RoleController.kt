package rocks.codesherpas.academy.accelerate.backend.kerberosservice.role

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.ResponseHandler
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.Permission
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionRepository
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionResourceWithId
import java.util.*

@RestController
class RoleController(
    private val roleRepository: RoleRepository,
    private val permissionRepository: PermissionRepository,
    private val responseHandler: ResponseHandler
) {

    @PostMapping("/roles")
    fun create(@RequestBody roleResource: RoleResource): ResponseEntity<Any> {
        val createdRole = Role(UUID.randomUUID().toString(), roleResource.description)

        val permissions = roleResource.permissions.map {
            val fetchedPermission = permissionRepository.findById(it.id)

            if (fetchedPermission.isPresent) {
                fetchedPermission.get()
            } else {
                val createdPermission = Permission(it.id, it.description)
                permissionRepository.save(createdPermission)
            }
        }

        createdRole.permissions += permissions

        val savedRole = roleRepository.save(createdRole)
        val roleToBeReturned = PermissionResourceWithId(savedRole.id, savedRole.description)

        return responseHandler
            .generateResponse("Permission created successfully", HttpStatus.CREATED, roleToBeReturned)
    }
}