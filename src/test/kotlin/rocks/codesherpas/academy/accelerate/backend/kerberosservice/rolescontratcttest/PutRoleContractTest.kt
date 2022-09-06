package rocks.codesherpas.academy.accelerate.backend.kerberosservice.rolescontratcttest

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
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionRepository
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.Role
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.RoleController
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.RoleRepository
import java.util.*

@WebMvcTest
class PutRoleContractTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    lateinit var permissionRepository: PermissionRepository
    @MockBean
    lateinit var roleRepository: RoleRepository

    @BeforeEach
    fun setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Test
    fun putRole() {
        val updatedRole = """
            {
                "description": "A new role",
                "permissions": []
            }
        """.trimIndent()
        val roleId = "123"

        Mockito.`when`(roleRepository.findById(roleId))
            .thenReturn(Optional.of(Role(roleId, "description")))

        Mockito.`when`(roleRepository.save(Mockito.any(Role::class.java)))
            .thenAnswer { invocation -> invocation.arguments[0] }

        RestAssuredMockMvc.given()
            .contentType("application/json")
            .body(updatedRole)
            .mockMvc(mockMvc)
            .standaloneSetup(RoleController(roleRepository, permissionRepository))
            .`when`()
            .put("/roles/{id}", roleId)
            .then()
            .statusCode(200)
            .contentType("application/json")
            .assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("singleRoleContract.json"))
    }
}