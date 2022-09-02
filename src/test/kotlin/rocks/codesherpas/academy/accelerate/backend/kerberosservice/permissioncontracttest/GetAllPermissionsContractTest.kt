package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permissioncontracttest

import io.restassured.RestAssured
import io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath
import io.restassured.module.mockmvc.RestAssuredMockMvc.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
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
class GetAllPermissionsContractTest(@Autowired val mockMvc: MockMvc) {

    @MockBean lateinit var permissionRepository: PermissionRepository
    @MockBean lateinit var roleRepository: RoleRepository

    @BeforeEach
    fun setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Test
    fun getAllPermissions() {

        `when`(permissionRepository.findAll())
            .thenReturn(listOf(
                Permission("123", "description1"),
                Permission("345", "description2")
            ))

        given()
            .mockMvc(mockMvc)
            .standaloneSetup(PermissionsController(permissionRepository, roleRepository))
        .`when`()
            .get("/permissions")
        .then()
            .statusCode(200)
            .contentType("application/json")
            .assertThat().body(matchesJsonSchemaInClasspath("getAllPermissionsContract.json"))
    }
}