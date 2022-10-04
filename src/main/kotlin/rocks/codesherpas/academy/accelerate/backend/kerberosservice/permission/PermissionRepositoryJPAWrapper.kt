package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission

import javax.inject.Named

@Named
class PermissionRepositoryJPAWrapper(private val permissionRepositoryJPA: PermissionRepositoryJPA): PermissionRepository {
    override fun findById(id: String): Permission? {
        val permission = permissionRepositoryJPA.findById(id)
        if (permission.isEmpty) {
            return null
        } else {
            return Permission(permission.get().id, permission.get().description)
        }
    }

    override fun save(permission: Permission) {
        val permissionJPA = PermissionJPA(permission.id, permission.description)

        permissionRepositoryJPA.save(permissionJPA)
    }

    override fun findAll(): List<Permission> {
        val permissionsJPA = permissionRepositoryJPA.findAll()

        return permissionsJPA.map { permissionJPA -> Permission(permissionJPA.id, permissionJPA.description) }
    }

    override fun deleteById(id: String) {
        permissionRepositoryJPA.deleteById(id)
    }
}