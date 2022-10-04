package rocks.codesherpas.academy.accelerate.backend.kerberosservice.rolescontratcttest

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
class PutRoleContractTest(@Autowired val mockMvc: MockMvc) {

    @MockBean
    lateinit var permissionRepository: PermissionRepositoryJPA

    @MockBean
    lateinit var roleRepository: RoleRepositoryJPA

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
    fun putRole() {
        val newDescription = "A new role"
        val updatedRole = """
            {
                "description": "$newDescription",
                "permissions": []
            }
        """.trimIndent()
        val roleId = "123"



        /*
        Mockito.`when`(roleRepository.findById(roleId))
            .thenReturn(Optional.of(RoleJPA(roleId, "description")))
        Mockito.`when`(roleRepository.save(Mockito.any(RoleJPA::class.java)))
            .thenAnswer { invocation -> invocation.arguments[0] }
        */
        Mockito.`when`(updateRole.execute(roleId, newDescription))
            .thenReturn(Role(roleId, newDescription, listOf("123", "234")))
        Mockito.`when`(getPermissionById.execute(anyString()))
            .thenReturn(Permission("123", "description"))

        RestAssuredMockMvc.given()
            .contentType("application/json")
            .body(updatedRole)
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
            .`when`()
            .put("/roles/{id}", roleId)
            .then()
            .statusCode(200)
            .contentType("application/json")
            .assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath("singleRoleContract.json"))
    }
}