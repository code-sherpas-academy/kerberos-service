package rocks.codesherpas.academy.accelerate.backend.kerberosservice.rolescontratcttest

import com.google.gson.JsonParser
import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Extract
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
class GetAllRolesContractTest(
    @Autowired val roleRepository: RoleRepository,
    @LocalServerPort val port: Int
) {
    private val role1 = "This is a role"
    private val roleId1 = "123"

    private val role2 = "This is another role"
    private val roleId2 = "1234"

    private val expectedJson = """[
            {
                "id": "$roleId1",
                "description": "$role1",
                "permissions": []
            },
            {
                "id": "$roleId2",
                "description": "$role2",
                "permissions": []
            }
        ]""".trimIndent()

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        roleRepository.deleteAll()

        val roles = listOf(Role(roleId1, role1), Role(roleId2, role2))
        roleRepository.saveAll(roles)
    }

    @AfterEach
    fun cleanUp() {
        roleRepository.deleteAll()
    }

    @Test
    fun getAllRoles() {
        When {
            get("/roles")
        } Then {
            statusCode(200)
            contentType("application/json")
        } Extract {
            val responseBody = JsonParser.parseString(body().asString())
            val expectedBody = JsonParser.parseString(expectedJson)
            Assertions.assertThat(responseBody).isEqualTo(expectedBody)
        }
    }
}