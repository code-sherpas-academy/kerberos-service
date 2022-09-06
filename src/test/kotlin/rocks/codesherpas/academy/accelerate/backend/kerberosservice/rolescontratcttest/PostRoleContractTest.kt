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

@WebMvcTest
class PostRoleContractTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    lateinit var permissionRepository: PermissionRepository
    @MockBean
    lateinit var roleRepository: RoleRepository

    private val requestBody = """
        {
            "description": "Role description",
            "permissions" : []
        }
    """.trimIndent()

    @BeforeEach
    fun setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Test
    fun createRole() {
        Mockito.`when`(roleRepository.save(Mockito.any(Role::class.java)))
            .thenAnswer { invocation -> invocation.arguments[0] }

        RestAssuredMockMvc.given()
            .mockMvc(mockMvc)
            .standaloneSetup(RoleController(roleRepository, permissionRepository))
            .contentType("application/json")
            .body(requestBody)
            .`when`()
            .post("/roles")
            .then()
            .statusCode(201)
            .contentType("application/json")
            .assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("singleRoleContract.json"))
    }
}