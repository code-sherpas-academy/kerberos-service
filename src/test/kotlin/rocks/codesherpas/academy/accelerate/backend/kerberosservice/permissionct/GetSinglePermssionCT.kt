package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permissionct

import com.google.gson.Gson
import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionResourceWithId

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GetSinglePermissionCT {

    @LocalServerPort
    val port:Int = 0

    private val description = "This is a permission"

    private val postRequestBody = """
        {
            "description":"$description"
        }
    """.trimIndent()

    private lateinit var savedPermissionId: String

    @BeforeEach
    fun setUp() {
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        Given {
            contentType("application/json")
            body(postRequestBody)
        } When {
            post("/permissions")
        } Extract {
            val responseBody = Gson().fromJson(body().asString(), PermissionResponse::class.java)
            savedPermissionId = responseBody.data.id
        }
    }

    @Test
    fun getSinglePermission() {
        Given {
            contentType("application/json")
        } When {
            get("/permissions/$savedPermissionId")
        } Then {
            statusCode(200)
        } Extract {
            val responseBody = Gson().fromJson(body().asString(), PermissionResponse::class.java)
            assertThat(responseBody.data.id).isEqualTo(savedPermissionId)
            assertThat(responseBody.data.description).isEqualTo(description)
        }
    }
}

data class PermissionResponse(
    val message: String,
    val status: Int,
    val data: PermissionResourceWithId
)