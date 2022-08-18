package rocks.codesherpas.academy.accelerate.backend.kerberosservice.role

import org.springframework.data.jpa.repository.JpaRepository

interface RoleRepository : JpaRepository<Role, String> {

}
