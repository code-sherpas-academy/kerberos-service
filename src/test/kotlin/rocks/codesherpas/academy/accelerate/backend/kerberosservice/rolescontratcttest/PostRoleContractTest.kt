package rocks.codesherpas.academy.accelerate.backend.kerberosservice.rolescontratcttest

import io.restassured.RestAssured
import io.restassured.module.jsv.JsonSchemaValidator
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.*
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.*

@WebMvcTest
class PostRoleContractTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    lateinit var permissionRepository: PermissionRepositoryJPA

    @MockBean
    lateinit var roleRepository: RoleRepositoryJPA

    @MockBean
    lateinit var assignPermissionToRole: AssignPermissionToRole

    @MockBean
    lateinit var getAllRoles: GetAllRoles

    @MockBean
    lateinit var updateRole: UpdateRole

    @MockBean
    lateinit var getRoleById: GetRoleById

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
        /*Mockito.`when`(roleRepository.save(Mockito.any(RoleJPA::class.java)))
            .thenAnswer { invocation -> invocation.arguments[0] }*/
        Mockito.`when`(createRole.execute(anyString(), anyList()))
            .thenReturn(Role("123", "description", listOf("123", "234")))
        Mockito.`when`(getPermissionById.execute(anyString()))
            .thenReturn(Permission("123", "description"))

        RestAssuredMockMvc.given()
            .mockMvc(mockMvc)
            .standaloneSetup(
                RoleController(
                    assignPermissionToRole,
                    getAllRoles,
                    getRoleById,
                    updateRole,
                    deleteRole,
                    createRole,
                    getPermissionById
                )
            )
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