package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permissionct

import io.restassured.RestAssured
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
class DeletePermissionCT(
    @Autowired val permissionRepository: PermissionRepositoryJPA,
    @LocalServerPort val port: Int
) {
    private val permissionId = "123"

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        permissionRepository.save(PermissionJPA(permissionId, "A description"))
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

        Assertions.assertThat(permissionRepository.existsById(permissionId)).isFalse
    }
}