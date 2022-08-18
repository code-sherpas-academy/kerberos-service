package rocks.codesherpas.academy.accelerate.backend.kerberosservice.role

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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
            .generateResponse("Role created successfully", HttpStatus.CREATED, roleToBeReturned)
    }

    @GetMapping("/roles/{id}")
    fun getOne(@PathVariable("id") id: String): ResponseEntity<Any> {
        val searchedRole = roleRepository.findById(id)

        return if (searchedRole.isPresent) {
            val foundRole = searchedRole.get()
            val permissions = foundRole.permissions.map { PermissionResourceWithId(it.id, it.description) }
            val roleToBeReturned = RoleResourceWithId(foundRole.id, foundRole.description, permissions)
            responseHandler
                .generateResponse("Role successfully retrieved", HttpStatus.OK, roleToBeReturned)
        } else {
            responseHandler.generateResponse("No role with id: $id", HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/roles")
    fun getAll(): ResponseEntity<Any> {
        val roles = roleRepository.findAll().map { role ->
            val permissions = role.permissions.map { PermissionResourceWithId(it.id, it.description) }
            RoleResourceWithId(role.id, role.description, permissions)
        }

        return responseHandler
            .generateResponse("Roles successfully retrieved", HttpStatus.OK, roles)
    }
}