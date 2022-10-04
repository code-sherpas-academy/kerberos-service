package rocks.codesherpas.academy.accelerate.backend.kerberosservice.role

import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionJPA
import javax.persistence.*

@Entity
@Table(name="role")
class RoleJPA(@Id val id: String, val description: String) {
    @ManyToMany
    @JoinTable(
        name = "role_permission",
        joinColumns = [JoinColumn(name = "role_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "permission_id", referencedColumnName = "id")]
    )
    val permissions = mutableListOf<PermissionJPA>()


    fun permissions(newItem: PermissionJPA) {
        permissions += newItem
    }
}
