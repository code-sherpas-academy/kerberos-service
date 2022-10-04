package rocks.codesherpas.academy.accelerate.backend.kerberosservice.role

interface RoleRepository {
    fun findById(roleId: String): Role?
    fun save(role: Role)
    fun findAll(): List<Role>
    fun delete(id: String)
}