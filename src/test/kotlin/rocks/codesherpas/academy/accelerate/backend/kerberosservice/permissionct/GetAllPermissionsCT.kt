package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permissionct

import com.google.gson.JsonParser
import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Extract
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GetAllPermissionsCT(
    @Autowired val permissionRepository: PermissionRepository,
    @LocalServerPort val port: Int
) {

    private val description1 = "This is a permission"
    private val permissionId1 = "123"

    private val description2 = "This is another permission"
    private val permissionId2 = "1234"

    private var expectedJson = ""

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        val previousPermissions = permissionRepository.findAll()
        expectedJson += '['
        previousPermissions.forEach{
            if (expectedJson.last() == '}') expectedJson += ','
            expectedJson += """{
                "id": "${it.id}",
                "description": "${it.description}"
            }""".trimIndent()
        }
        if (expectedJson.last() == '}') expectedJson += ','
        expectedJson += """{
                "id": "$permissionId1",
                "description": "$description1"
            },
            {
                "id": "$permissionId2",
                "description": "$description2"
            }]""".trimIndent()
        val permissions = listOf(Permission(permissionId1, description1), Permission(permissionId2, description2))
        permissionRepository.saveAll(permissions)
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