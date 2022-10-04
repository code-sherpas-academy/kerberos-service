package rocks.codesherpas.academy.accelerate.backend.kerberosservice.rolescontratcttest

import io.restassured.RestAssured
import io.restassured.module.mockmvc.RestAssuredMockMvc
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.*
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.*
import java.util.*

@WebMvcTest
class DeleteRoleContractTest(@Autowired val mockMvc: MockMvc) {

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
    fun deleteRole() {
        val roleId = "123"

        Mockito.`when`(roleRepository.findById(roleId))
            .thenReturn(Optional.of(RoleJPA(roleId, "description")))

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
            .`when`()
            .delete("/roles/{id}", roleId)
            .then()
            .statusCode(204)
    }
}