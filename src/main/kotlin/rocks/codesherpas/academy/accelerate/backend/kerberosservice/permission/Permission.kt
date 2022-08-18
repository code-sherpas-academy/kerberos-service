package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission

import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.Role
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.ManyToMany

@Entity
class Permission(@Id val id: String, val description: String) {
    @ManyToMany(mappedBy = "permissions")
    private val roles: Collection<Role>? = null
}
