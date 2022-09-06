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
class GetAllRolesContractTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    lateinit var permissionRepository: PermissionRepository
    @MockBean
    lateinit var roleRepository: RoleRepository

    @BeforeEach
    fun setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Test
    fun getAllRoles() {
        Mockito.`when`(roleRepository.findAll())
            .thenReturn(listOf(
                Role("123", "description1"),
                Role("345", "description2")
            ))

        RestAssuredMockMvc.given()
            .mockMvc(mockMvc)
            .standaloneSetup(RoleController(roleRepository, permissionRepository))
            .`when`()
            .get("/roles/")
            .then()
            .statusCode(200)
            .contentType("application/json")
            .assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("getAllRolesContract.json"))
    }
}