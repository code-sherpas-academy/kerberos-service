package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.RoleRepositoryJPA

@RestController
class PermissionsController(
    private val createPermission: CreatePermission,
    private val getAllPermissions: GetAllPermissions,
    private val getPermissionByID: GetPermissionById,
    private val deletePermission: DeletePermission,
    private val updatePermission: UpdatePermission
) {

    @GetMapping("/permissions")
    fun getAll(): List<PermissionResourceWithId> {
        val permissions: List<Permission> = getAllPermissions.execute()
        return permissions.map { permission -> PermissionResourceWithId(permission.id, permission.description) }
    }

    @GetMapping("/permissions/{id}")
    fun getOne(@PathVariable("id") id: String): PermissionResourceWithId {

        val permission = getPermissionByID.execute(id)

        return if (permission != null) {
            PermissionResourceWithId(permission.id, permission.description)
        } else {
            throw Exception("No permission with id: $id")
            // throw http exception ("No permission with id: $id", HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/permissions")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody permissionResource: PermissionResource): PermissionResourceWithId {
        val createdPermission = createPermission.execute(permissionResource.description)
        return PermissionResourceWithId(createdPermission.id, createdPermission.description)
    }

    @PutMapping("/permissions/{id}")
    fun update(
        @RequestBody permissionResource: PermissionResource,
        @PathVariable("id") id: String
    ): PermissionResourceWithId {
        val updatedPermission  = updatePermission.execute(id, permissionResource.description)
        return PermissionResourceWithId(updatedPermission.id, updatedPermission.description)
    }

    @DeleteMapping("/permissions/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable("id") id: String) {
        deletePermission.execute(id)
    }
}