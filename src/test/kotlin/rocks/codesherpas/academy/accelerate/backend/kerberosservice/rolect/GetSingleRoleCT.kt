package rocks.codesherpas.academy.accelerate.backend.kerberosservice.rolect

import com.google.gson.JsonParser
import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Extract
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
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionJPA
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionRepositoryJPA
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.RoleJPA
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.RoleRepositoryJPA

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GetSingleRoleCT(
    @Autowired val roleRepository: RoleRepositoryJPA,
    @Autowired val permissionRepository: PermissionRepositoryJPA,
    @LocalServerPort val port: Int
) {

    private val roleId = "123"
    private val roleDescription = "This is a role"
    private val permissionId = "456"
    private val permissionDescription = "This is a description"
    private val expectedJson = """
        {
            "id": "$roleId",
            "description": "$roleDescription",
            "permissions": [
                {
                    "id": "$permissionId",
                    "description": "$permissionDescription"
                }
            ]
        }
    """.trimIndent()

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        roleRepository.deleteAll()
        permissionRepository.deleteAll()

        val role = RoleJPA(roleId, roleDescription)
        val permission = PermissionJPA(permissionId, permissionDescription)
        permissionRepository.save(permission)
        role.permissions.add(permission)

        roleRepository.save(role)
    }

    @AfterEach
    fun cleanUp() {
        roleRepository.deleteAll()
        permissionRepository.deleteAll()
    }

    @Test
    fun getSingleRole() {
        Given {
            pathParam("id", roleId)
        } When {
            get("/roles/{id}")
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