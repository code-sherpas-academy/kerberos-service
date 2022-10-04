package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission

import org.springframework.data.jpa.repository.JpaRepository

interface PermissionRepositoryJPA : JpaRepository<PermissionJPA, String> {

}