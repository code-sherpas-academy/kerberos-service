package rocks.codesherpas.academy.accelerate.backend.kerberosservice.rolect

import io.restassured.RestAssured
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
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionRepository
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionRepositoryJPA
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.RoleJPA
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.RoleRepositoryJPA

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DeleteRoleCT(
    @Autowired val roleRepository: RoleRepositoryJPA,
    @LocalServerPort val port: Int
) {

    private val roleId = "123"

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        roleRepository.deleteAll()

        roleRepository.save(RoleJPA(roleId, "A description"))
    }

    @AfterEach
    fun cleanUp() {
        roleRepository.deleteAll()
    }

    @Test
    fun deletePermission() {
        Given {
            pathParam("id", roleId)
        } When {
            delete("/roles/{id}")
        } Then {
            statusCode(204)
        }

        Assertions.assertThat(roleRepository.existsById(roleId)).isFalse
    }
}