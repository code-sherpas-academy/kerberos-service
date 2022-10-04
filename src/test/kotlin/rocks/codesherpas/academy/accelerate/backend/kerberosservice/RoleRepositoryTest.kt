package rocks.codesherpas.academy.accelerate.backend.kerberosservice

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.RoleJPA
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.RoleRepositoryJPA

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoleRepositoryTest(@Autowired private val roleRepository: RoleRepositoryJPA) {
    private lateinit var id: String
    private lateinit var role: RoleJPA

    @BeforeEach
    fun setUp() {
        id = "1234"
        role = RoleJPA(id, "A role")
        roleRepository.save(role)
    }

    @Test
    fun `finds a role by id`() {
        val retrievedRole = roleRepository.findById(id)

        assertThat(retrievedRole.get()).usingRecursiveComparison().isEqualTo(role)
    }

    @Test
    fun `finds all roles`() {
        val previousRoles = roleRepository.findAll()

        val savedRoles = listOf(
            RoleJPA("1", "Role 1"),
            RoleJPA("2", "Role 2")
        )

        roleRepository.saveAll(savedRoles)

        val retrievedRoles = roleRepository.findAll()

        assertThat(retrievedRoles)
            .usingRecursiveFieldByFieldElementComparator()
            .containsExactlyInAnyOrderElementsOf(previousRoles + savedRoles)
    }

    @Test
    fun `saves a role`() {
        val roleToBeSaved = RoleJPA(id + "4", "A role")
        val searchedRole = roleRepository.findById(roleToBeSaved.id)
        assertThat(searchedRole.isEmpty).isTrue

        roleRepository.save(roleToBeSaved)

        val savedRole = roleRepository.findById(roleToBeSaved.id).get()
        assertThat(savedRole).usingRecursiveComparison().isEqualTo(roleToBeSaved)
    }

    @Test
    fun `deletes a role by id`() {
        assertThat(roleRepository.findById(id).isPresent).isTrue

        roleRepository.deleteById(id)

        assertThat(roleRepository.findById(id).isEmpty).isTrue
    }
}