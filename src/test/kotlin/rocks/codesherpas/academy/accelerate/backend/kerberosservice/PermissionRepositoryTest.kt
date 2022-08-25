package rocks.codesherpas.academy.accelerate.backend.kerberosservice

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.Permission
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionRepository

@DataJpaTest
class PermissionRepositoryTest(@Autowired private val permissionRepository: PermissionRepository) {
    @Test
    fun `finds a permission by id`() {
        val id = "123"
        val permission = Permission(id, "description")
        permissionRepository.save(permission)

        val retrievedPermission = permissionRepository.findById(id)

        assertThat(retrievedPermission.get()).usingRecursiveComparison().isEqualTo(permission)
    }

    @Test
    fun `finds all permissions`() {
        val previousPermissions = permissionRepository.findAll()

        val savedPermissions = listOf(
            Permission("1", "Permission 1"),
            Permission("2", "Permission 2")
        )

        permissionRepository.saveAll(savedPermissions)

        val retrievedPermissions = permissionRepository.findAll()

        assertThat(retrievedPermissions)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrderElementsOf(savedPermissions + previousPermissions)
    }
}