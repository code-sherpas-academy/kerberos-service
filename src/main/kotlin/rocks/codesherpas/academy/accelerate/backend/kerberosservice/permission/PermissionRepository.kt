package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission

interface PermissionRepository {
    fun findById(id: String): Permission?
    fun save(permission: Permission)
    fun findAll(): List<Permission>
    fun deleteById(id: String)
}
