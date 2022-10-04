package rocks.codesherpas.academy.accelerate.backend.kerberosservice.role

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.GetPermissionById
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionResourceWithId

@RestController
class RoleController(
    private val assignPermissionToRole: AssignPermissionToRole,
    private val getAllRoles: GetAllRoles,
    private val getRoleById: GetRoleById,
    private val updateRole: UpdateRole,
    private val deleteRole: DeleteRole,
    private val createRole: CreateRole,
    private val getPermissionById: GetPermissionById
) {

    @PostMapping("/roles")
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@RequestBody roleResource: RoleResource): RoleResourceWithId {
        val permissionIds = roleResource.permissions.map { it.id }
        val savedRole = createRole.execute(roleResource.description, permissionIds)
        val permissions = getPermissionResources(savedRole)

        return RoleResourceWithId(savedRole.id, savedRole.description, permissions)
    }

    @GetMapping("/roles/{id}")
    fun getOne(@PathVariable("id") id: String): RoleResourceWithId {
        val searchedRole = getRoleById.execute(id)

        return if (searchedRole != null) {
            val permissions = getPermissionResources(searchedRole)

            RoleResourceWithId(searchedRole.id, searchedRole.description, permissions)
        } else {
            throw Exception("No role with id: $id")
            // throw http exception ("No role with id: $id", HttpStatus.NOT_FOUND)
        }
    }

    @GetMapping("/roles")
    fun getAll(): List<RoleResourceWithId> {
        return getAllRoles.execute().map { role ->
            val permissions = getPermissionResources(role)
            RoleResourceWithId(role.id, role.description, permissions)
        }
    }

    @DeleteMapping("roles/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun delete(@PathVariable("id") id: String) {
        deleteRole.execute(id)
    }

    @PutMapping("/roles/{id}")
    fun update(
        @RequestBody roleResource: RoleResource,
        @PathVariable("id") id: String
    ): RoleResourceWithId {
        val updatedRole = updateRole.execute(id, roleResource.description)
        val permissions = getPermissionResources(updatedRole)

        return RoleResourceWithId(updatedRole.id, updatedRole.description, permissions)
    }

    @PostMapping("/roles/{roleId}/add-permission/{permissionId}")
    fun addPermission(
        @PathVariable("roleId") roleId: String,
        @PathVariable("permissionId") permissionId: String
    ) {
        assignPermissionToRole.execute(roleId, permissionId)
    }

    private fun getPermissionResources(role: Role): List<PermissionResourceWithId> {
        return role.permissions.map {
            val permission = getPermissionById.execute(it)
            if (permission === null) throw Exception("No permission with Id $it")
            PermissionResourceWithId(permission.id, permission.description)
        }
    }
}