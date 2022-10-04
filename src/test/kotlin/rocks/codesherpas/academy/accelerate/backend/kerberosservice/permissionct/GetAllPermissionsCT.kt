package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permissionct

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
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionJPA
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionRepositoryJPA

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GetAllPermissionsCT(
    @Autowired val permissionRepository: PermissionRepositoryJPA,
    @LocalServerPort val port: Int
) {

    private val description1 = "This is a permission"
    private val permissionId1 = "123"

    private val description2 = "This is another permission"
    private val permissionId2 = "1234"

    private val expectedJson = """[
            {
                "id": "$permissionId1",
                "description": "$description1"
            },
            {
                "id": "$permissionId2",
                "description": "$description2"
            }
        ]""".trimIndent()

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        permissionRepository.deleteAll()

        val permissions = listOf(PermissionJPA(permissionId1, description1), PermissionJPA(permissionId2, description2))
        permissionRepository.saveAll(permissions)
    }

    @AfterEach
    fun cleanUp() {
        permissionRepository.deleteAll()
    }

    @Test
    fun getAllPermissions() {
        When {
            get("/permissions")
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