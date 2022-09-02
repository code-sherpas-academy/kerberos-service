package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permissioncontracttest

import io.restassured.RestAssured
import io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.any
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.Permission
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionRepository
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionsController
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.RoleRepository


@WebMvcTest
class PostPermissionContractTest(@Autowired val mockMvc: MockMvc) {

    @MockBean lateinit var permissionRepository: PermissionRepository
    @MockBean lateinit var roleRepository: RoleRepository

    private val requestBody = """
        {
            "description": "Permission description"
        }
    """.trimIndent()

    @BeforeEach
    fun setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Test
    fun createPermission() {
        `when`(permissionRepository.save(any(Permission::class.java)))
            .thenAnswer { invocation -> invocation.arguments[0] }

        given()
            .mockMvc(mockMvc)
            .standaloneSetup(PermissionsController(permissionRepository, roleRepository))
            .contentType("application/json")
            .body(requestBody)
        .`when`()
            .post("/permissions")
        .then()
            .statusCode(201)
            .contentType("application/json")
            .assertThat().body(matchesJsonSchemaInClasspath("singlePermissionContract.json"))
    }
}