package rocks.codesherpas.academy.accelerate.backend.kerberosservice.role

import javax.inject.Named

// Class is open because Mockito does not allow mocking final classes
@Named
open class AssignPermissionToRole(val roleRepository: RoleRepository) {
    fun execute(permissionId: String, roleId: String) {
        val role: Role = roleRepository.findById(roleId) ?: throw Exception("Role with id $roleId not found")
        role.addPermission(permissionId)
        roleRepository.save(role)
    }
}