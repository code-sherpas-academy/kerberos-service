package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permissionct

import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.Permission
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionRepository

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeletePermissionCT(
    @Autowired val permissionRepository: PermissionRepository,
    @LocalServerPort val port: Int
) {
    private val permissionId = "123"

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        permissionRepository.save(Permission(permissionId, "A description"))
    }

    @Test
    fun deletePermission() {
        Given {
            pathParam("id", permissionId)
        } When {
            delete("/permissions/{id}")
        } Then {
            statusCode(204)
        }

        Given {
            pathParam("id", permissionId)
        } When {
            get("/permissions/{id}")
        } Then {
            // This should be changed to 404 when error handling is resolved
            statusCode(500)
        }
    }
}