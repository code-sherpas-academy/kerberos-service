package rocks.codesherpas.academy.accelerate.backend.kerberosservice.role

import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.Permission
import javax.persistence.*

@Entity
class Role(@Id val id: String, val description: String) {
    @ManyToMany
    @JoinTable(
        name = "roles_permissions",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "permission_id", referencedColumnName = "id")]
    )
    val permissions = mutableListOf<Permission>()


    fun permissions(newItem: Permission) {
        permissions += newItem
    }
}
