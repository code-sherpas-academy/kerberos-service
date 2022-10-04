package rocks.codesherpas.academy.accelerate.backend.kerberosservice.role

import org.springframework.data.repository.findByIdOrNull
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionRepositoryJPA
import javax.inject.Named

@Named // Or Component??
class RoleRepositoryJPAWrapper(
    private val roleRepositoryJPA: RoleRepositoryJPA,
    private val permissionRepositoryJPA: PermissionRepositoryJPA
) : RoleRepository {
    override fun findById(roleId: String): Role? {
        val roleJPA = roleRepositoryJPA.findByIdOrNull(roleId)

        val role = if (roleJPA != null) {
            val permissionIds = roleJPA.permissions.map { it.id }
            Role(roleJPA.id, roleJPA.description, permissionIds)
        } else null

        return role
    }

    override fun save(role: Role) {
        val roleJPA = RoleJPA(role.id, role.description)

        // What happens if there are permissions ids not found by permissionRepositoryJPA??
        val permissions = permissionRepositoryJPA.findAllById(role.permissions)
        roleJPA.permissions += permissions
        roleRepositoryJPA.save(roleJPA)
    }

    override fun findAll(): List<Role> {
        val rolesJPA = roleRepositoryJPA.findAll()

        return rolesJPA.map { Role(it.id, it.description, it.permissions.map { it.id }) }
    }

    override fun delete(id: String) {
        roleRepositoryJPA.deleteById(id)
    }
}
