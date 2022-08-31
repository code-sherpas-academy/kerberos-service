package rocks.codesherpas.academy.accelerate.backend.kerberosservice.rolect

import com.google.gson.JsonParser
import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.Permission
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionRepository
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.Role
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.RoleRepository

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GetAllRolesCT(
    @Autowired
    val roleRepository: RoleRepository,
    @Autowired
    val permissionRepository: PermissionRepository,
    @LocalServerPort
    val port: Int
) {

    private val roleDescription1 = "This is a new role"
    private val roleId1 = "123"

    private val roleDescription2 = "This is another role"
    private val roleId2 = "1234"

    private var expectedJson = """[{
                "id": "$roleId1",
                "description": "$roleDescription1",
                "permissions": []
            },
            {
                "id": "$roleId2",
                "description": "$roleDescription2",
                "permissions": []
            }]""".trimIndent()

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        val roles = listOf(Role(roleId1, roleDescription1), Role(roleId2, roleDescription2))
        roleRepository.saveAll(roles)
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