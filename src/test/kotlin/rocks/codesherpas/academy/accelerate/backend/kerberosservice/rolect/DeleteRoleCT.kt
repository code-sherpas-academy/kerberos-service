package rocks.codesherpas.academy.accelerate.backend.kerberosservice.rolect

import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
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

        roleRepository.save(Role(roleId, "A description"))
    }

    @Test
    fun deletePermission() {
        Given {
            pathParam("id", roleId)
        } When {
            delete("/roles/{id}")
        } Then {
            statusCode(200)
        }

        Given {
            pathParam("id", roleId)
        } When {
            get("/roles/{id}")
        } Then {
            // This should be changed to 404 when error handling is resolved
            statusCode(500)
        }
    }
}