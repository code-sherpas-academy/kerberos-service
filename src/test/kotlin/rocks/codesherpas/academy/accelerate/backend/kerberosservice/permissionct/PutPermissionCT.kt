package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permissionct

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
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionJPA
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionRepositoryJPA

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PutPermissionCT(
    @Autowired val permissionRepository: PermissionRepositoryJPA,
    @LocalServerPort val port: Int
) {

    private val permissionId = "123"
    private val updatedDescription = "Updated description"
    private val expectedJson = """
        {
            "id": "$permissionId",
            "description": "$updatedDescription"
        }
    """.trimIndent()

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        permissionRepository.save(PermissionJPA(permissionId, "Initial description"))
    }

    @Test
    fun updatePermission() {
        val updatedPermission = """
            {
                "description": "$updatedDescription"
            }
        """.trimIndent()

        Given {
            pathParam("id", permissionId)
            contentType("application/json")
            body(updatedPermission)
        } When {
            put("/permissions/{id}")
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