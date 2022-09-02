package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permissioncontracttest

import io.restassured.RestAssured
import io.restassured.module.jsv.JsonSchemaValidator
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.Permission
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionRepository
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionsController
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.RoleRepository
import java.util.*

@WebMvcTest
class PutPermissionsContractTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    lateinit var permissionRepository: PermissionRepository
    @MockBean
    lateinit var roleRepository: RoleRepository

    @BeforeEach
    fun setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Test
    fun putPermission() {
        val updatedPermission = """
            {
                "description": "A new description"
            }
        """.trimIndent()
        val permissionId = "123"

        Mockito.`when`(permissionRepository.findById(permissionId))
            .thenReturn(Optional.of(Permission(permissionId, "description")))

        Mockito.`when`(permissionRepository.save(Mockito.any(Permission::class.java)))
            .thenAnswer { i -> i.arguments[0] }

        RestAssuredMockMvc.given()
            .contentType("application/json")
            .body(updatedPermission)
            .mockMvc(mockMvc)
            .standaloneSetup(PermissionsController(permissionRepository, roleRepository))
            .`when`()
            .put("/permissions/{id}", permissionId)
            .then()
            .statusCode(200)
            .contentType("application/json")
            .assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("singlePermissionContract.json"))
    }
}