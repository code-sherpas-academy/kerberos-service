package rocks.codesherpas.academy.accelerate.backend.kerberosservice

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionJPA
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionRepositoryJPA

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PermissionRepositoryTest(@Autowired private val permissionRepository: PermissionRepositoryJPA) {

    private lateinit var id: String
    private lateinit var permission: PermissionJPA

    @BeforeEach
    fun setUp() {
        id = "123"
        permission = PermissionJPA(id, "description")
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
            PermissionJPA("1", "Permission 1"),
            PermissionJPA("2", "Permission 2")
        )

        permissionRepository.saveAll(savedPermissions)

        val retrievedPermissions = permissionRepository.findAll()

        assertThat(retrievedPermissions)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrderElementsOf(savedPermissions + previousPermissions)
    }

    @Test
    fun `saves a permission`() {
        val permissionToBeSaved = PermissionJPA(id + "4", "A permission")
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