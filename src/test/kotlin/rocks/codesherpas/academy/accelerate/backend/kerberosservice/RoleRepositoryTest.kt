package rocks.codesherpas.academy.accelerate.backend.kerberosservice

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.Role
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.RoleRepository

@DataJpaTest
class RoleRepositoryTest(@Autowired private val roleRepository: RoleRepository) {
    private lateinit var id: String
    private lateinit var role: Role

    @BeforeEach
    fun setUp() {
        id = "1234"
        role = Role(id, "A role")
        roleRepository.save(role)
    }

    @Test
    fun `finds a role by id`() {
        val retrievedRole = roleRepository.findById(id)

        assertThat(retrievedRole.get()).usingRecursiveComparison().isEqualTo(role)
    }
}