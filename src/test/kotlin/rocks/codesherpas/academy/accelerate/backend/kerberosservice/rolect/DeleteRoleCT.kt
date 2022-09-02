package rocks.codesherpas.academy.accelerate.backend.kerberosservice.rolect

import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.Role
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.RoleRepository

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeleteRoleCT(
    @Autowired val roleRepository: RoleRepository,
    @LocalServerPort val port: Int
) {

    private val roleId = "123"

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        roleRepository.deleteAll()

        roleRepository.save(Role(roleId, "A description"))
    }

    @AfterEach
    fun cleanUp() {
        roleRepository.deleteAll()
    }

    @Test
    fun deletePermission() {
        Given {
            pathParam("id", roleId)
        } When {
            delete("/roles/{id}")
        } Then {
            statusCode(204)
        }

        Assertions.assertThat(roleRepository.existsById(roleId)).isFalse
    }
}