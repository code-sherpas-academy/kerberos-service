package rocks.codesherpas.academy.accelerate.backend.kerberosservice

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.Permission
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionRepository

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PermissionRepositoryTest(@Autowired private val permissionRepository: PermissionRepository) {

    private lateinit var id: String
    private lateinit var permission: Permission

    @BeforeEach
    fun setUp() {
        id = "123"
        permission = Permission(id, "description")
        permissionRepository.save(permission)
    }

    @Test
    fun `finds a permission by id`() {
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

    @Test
    fun `saves a permission`() {
        val permissionToBeSaved = Permission(id + "4", "A permission")
        val searchedPermission = permissionRepository.findById(permissionToBeSaved.id)
        assertThat(searchedPermission.isEmpty).isTrue

        permissionRepository.save(permissionToBeSaved)

        val savedPermission = permissionRepository.findById(permissionToBeSaved.id)
        assertThat(savedPermission.get()).usingRecursiveComparison().isEqualTo(permissionToBeSaved)
    }

    @Test
    fun `deletes a permission by id`() {
        assertThat(permissionRepository.findById(id).isPresent).isTrue

        permissionRepository.deleteById(id)

        assertThat(permissionRepository.findById(id).isEmpty).isTrue
    }
}