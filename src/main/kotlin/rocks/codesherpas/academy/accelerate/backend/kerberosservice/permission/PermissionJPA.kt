package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission

import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.RoleJPA
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToMany
import javax.persistence.Table

@Entity
@Table(name="permission")
class PermissionJPA(@Id val id: String, val description: String) {
    @ManyToMany(mappedBy = "permissions")
    private val roles: Collection<RoleJPA>? = null
}
