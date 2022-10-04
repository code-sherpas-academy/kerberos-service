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
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.RoleJPA
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.RoleRepositoryJPA


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PutRoleCT(
    @Autowired val roleRepository: RoleRepositoryJPA,
    @LocalServerPort val port: Int
) {

    private val roleId = "123"
    private val updatedDescription = "Updated description"
    private val expectedJson = """
        {
            "id": "$roleId",
            "description": "$updatedDescription",
            "permissions": []
        }
    """.trimIndent()

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        roleRepository.deleteAll()

        roleRepository.save(RoleJPA(roleId, "Initial description"))
    }

    @AfterEach
    fun cleanUp() {
        roleRepository.deleteAll()
    }

    @Test
    fun updateRole() {
        val updatedPermission = """
            {
                "description": "$updatedDescription",
                "permissions": []
            }
        """.trimIndent()

        Given {
            pathParam("id", roleId)
            contentType("application/json")
            body(updatedPermission)
        } When {
            put("/roles/{id}")
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