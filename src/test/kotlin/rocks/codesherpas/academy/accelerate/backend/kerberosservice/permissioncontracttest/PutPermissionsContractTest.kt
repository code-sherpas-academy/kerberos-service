package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permissioncontracttest

import io.restassured.RestAssured
import io.restassured.module.jsv.JsonSchemaValidator
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.*
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.*

@WebMvcTest
class PutPermissionsContractTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    lateinit var permissionRepository: PermissionRepositoryJPA

    @MockBean
    lateinit var roleRepository: RoleRepositoryJPA

    // Required because test failed because this Bean could not be injected to RoleController
    // Strange because this test uses PermissionController, not RoleController
    @MockBean
    lateinit var assignPermissionToRole: AssignPermissionToRole

    @MockBean
    lateinit var getAllRoles: GetAllRoles

    @MockBean
    lateinit var getRoleById: GetRoleById

    @MockBean
    lateinit var updateRole: UpdateRole

    @MockBean
    lateinit var deleteRole: DeleteRole

    @MockBean
    lateinit var createRole: CreateRole

    @MockBean
    lateinit var getPermissionById: GetPermissionById

    @MockBean
    lateinit var createPermission: CreatePermission

    @MockBean
    lateinit var getAllPermissions: GetAllPermissions

    @MockBean
    lateinit var deletePermission: DeletePermission

    @MockBean
    lateinit var updatePermission: UpdatePermission

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

        Mockito.`when`(updatePermission.execute(anyString(), anyString()))
            .thenReturn(Permission(permissionId, "description"))

        RestAssuredMockMvc.given()
            .contentType("application/json")
            .body(updatedPermission)
            .mockMvc(mockMvc)
            .standaloneSetup(
                PermissionsController(
                    createPermission,
                    getAllPermissions,
                    getPermissionById,
                    deletePermission,
                    updatePermission
                )
            )
        .`when`()
            .put("/permissions/{id}", permissionId)
        .then()
            .statusCode(200)
            .contentType("application/json")
            .assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("singlePermissionContract.json"))
    }
}