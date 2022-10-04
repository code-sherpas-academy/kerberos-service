package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permissioncontracttest

import io.restassured.RestAssured
import io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.*
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.*


@WebMvcTest
class PostPermissionContractTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    lateinit var permissionRepository: PermissionRepositoryJPA

    @MockBean
    lateinit var roleRepository: RoleRepositoryJPA

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

    // Required because test failed because this Bean could not be injected to RoleController
    // Strange because this test uses PermissionController, not RoleController
    @MockBean
    lateinit var assignPermissionToRole: AssignPermissionToRole

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
        /*`when`(permissionRepository.save(any(PermissionJPA::class.java)))
            .thenAnswer { invocation -> invocation.arguments[0] }*/
       `when`(createPermission.execute(anyString()))
           .thenAnswer { Permission("123", "description") }

        given()
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