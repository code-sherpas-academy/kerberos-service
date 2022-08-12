package rocks.codesherpas.academy.accelerate.backend.kerberosservice

import org.springframework.data.jpa.repository.JpaRepository

interface PermissionRepository : JpaRepository<Permission, String> {

}