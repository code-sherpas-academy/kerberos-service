package rocks.codesherpas.academy.accelerate.backend.kerberosservice.role

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.Permission
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionRepository
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionResourceWithId
import java.util.*

@RestController
class RoleController(
    private val roleRepository: RoleRepository,
    private val permissionRepository: PermissionRepository
) {

    @PostMapping("/roles")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody roleResource: RoleResource): RoleResourceWithId {
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
        val permissionsToBeReturned = savedRole.permissions.map { PermissionResourceWithId(it.id, it.description) }
        return RoleResourceWithId(savedRole.id, savedRole.description, permissionsToBeReturned)
    }

    @GetMapping("/roles/{id}")
    fun getOne(@PathVariable("id") id: String): RoleResourceWithId {
        val searchedRole = roleRepository.findById(id)

        return if (searchedRole.isPresent) {
            val foundRole = searchedRole.get()
            val permissions = foundRole.permissions.map { PermissionResourceWithId(it.id, it.description) }
            RoleResourceWithId(foundRole.id, foundRole.description, permissions)
        } else {
            throw Exception("No role with id: $id")
            // throw http exception ("No role with id: $id", HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/roles")
    fun getAll(): List<RoleResourceWithId> {
        return roleRepository.findAll().map { role ->
            val permissions = role.permissions.map { PermissionResourceWithId(it.id, it.description) }
            RoleResourceWithId(role.id, role.description, permissions)
        }
    }

    @DeleteMapping("roles/{id}")
    fun delete(@PathVariable("id") id: String) {
        val roleToBeDeleted = roleRepository.findById(id)

        if (roleToBeDeleted.isPresent){
            roleRepository.deleteById(id)
        } else {
            throw Exception("No role with id: $id")
            // throw http exception ("No role with id: $id", HttpStatus.NOT_FOUND)
        }
    }

    @PutMapping("/roles/{id}")
    fun update(
        @RequestBody roleResource: RoleResource,
        @PathVariable("id") id: String
    ): RoleResourceWithId {
        val roleToBeUpdated = roleRepository.findById(id)

        return if (roleToBeUpdated.isPresent) {
            val updatedRole = Role(id, roleResource.description)

            val permissions = roleResource.permissions.map {
                val fetchedPermission = permissionRepository.findById(it.id)

                if (fetchedPermission.isPresent) {
                    fetchedPermission.get()
                } else {
                    val createdPermission = Permission(it.id, it.description)
                    permissionRepository.save(createdPermission)
                }
            }

            updatedRole.permissions += permissions

            val savedRole = roleRepository.save(updatedRole)
            val permissionsToBeReturned = savedRole.permissions.map { PermissionResourceWithId(it.id, it.description) }
            RoleResourceWithId(savedRole.id, savedRole.description, permissionsToBeReturned)
        } else {
            throw Exception("No role with id: $id")
            // throw http exception ("No role with id: $id", HttpStatus.NOT_FOUND)
        }
    }
}