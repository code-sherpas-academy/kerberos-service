package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.RoleRepository
import java.util.*

@RestController
class PermissionsController(
    private val permissionRepository: PermissionRepository,
    private val roleRepository: RoleRepository
) {

    @GetMapping("/permissions")
    fun getAll(): List<PermissionResourceWithId> {
        return permissionRepository.findAll()
            .map { permission -> PermissionResourceWithId(permission.id, permission.description) }
    }

    @GetMapping("/permissions/{id}")
    fun getOne(@PathVariable("id") id: String): PermissionResourceWithId {
        val searchedPermission = permissionRepository.findById(id)

        return if (searchedPermission.isPresent) {
            val foundPermission = searchedPermission.get()
            PermissionResourceWithId(foundPermission.id, foundPermission.description)
        } else {
            throw Exception("No permission with id: $id")
            // throw http exception ("No permission with id: $id", HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/permissions")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody permissionResource: PermissionResource): PermissionResourceWithId {
        val createdPermission = Permission(UUID.randomUUID().toString(), permissionResource.description)
        val savedPermission = permissionRepository.save(createdPermission)
        return PermissionResourceWithId(savedPermission.id, savedPermission.description)
    }

    @PutMapping("/permissions/{id}")
    fun update(
        @RequestBody permissionResource: PermissionResource,
        @PathVariable("id") id: String
    ): PermissionResourceWithId {
        val permissionToBeUpdated = permissionRepository.findById(id)

        return if (permissionToBeUpdated.isPresent) {
            val updatedPermission = Permission(id, permissionResource.description)
            val savedPermission = permissionRepository.save(updatedPermission)
            PermissionResourceWithId(savedPermission.id, savedPermission.description)
        } else {
            throw Exception("No permission with id: $id")
            // throw http exception ("No permission with id: $id", HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("/permissions/{id}")
    fun delete(@PathVariable("id") id: String) {
        val permissionToBeDeleted = permissionRepository.findById(id)

        if (permissionToBeDeleted.isPresent){
            val roles = roleRepository.findAll()
            roles.forEach{ role ->
                role.permissions.removeIf{
                    it.id == id
                }
            }
            roleRepository.saveAll(roles)
            permissionRepository.deleteById(id)
        } else {
            throw Exception("No permission with id: $id")
            // throw http exception ("No permission with id: $id", HttpStatus.NOT_FOUND)
        }
    }
}