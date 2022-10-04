package rocks.codesherpas.academy.accelerate.backend.kerberosservice.role

data class Role(val id: String, val description: String, val permissions: List<String>) {
    fun addPermission(permissionId: String): Role {
        return this.copy(permissions = this.permissions + permissionId)
    }

    fun deletePermission(permissionId: String): Role {
        return this.copy(permissions = this.permissions - permissionId)
    }
}
