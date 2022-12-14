package rocks.codesherpas.academy.accelerate.backend.kerberosservice.rolect

import com.google.gson.JsonParser
import io.mockk.every
import io.mockk.mockkStatic
import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import rocks.codesherpas.academy.accelerate.backend.kerberosservice.role.RoleRepositoryJPA
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostRoleCT(
    @LocalServerPort val port:Int,
    @Autowired val roleRepository: RoleRepositoryJPA
) {

    private final val uuid: UUID = UUID.randomUUID()
    private val description = "This is a post role"

    val expectedJson = """
        {
            "id": "$uuid",
            "description": "$description",
            "permissions": []
        }
    """.trimIndent()

    var requestBody:String = """
        {
            "description": "$description",
            "permissions": []
        }
    """.trimIndent()

    @BeforeEach
    fun setUp(){
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

        roleRepository.deleteAll()
    }

    @AfterEach
    fun cleanUp() {
        roleRepository.deleteAll()
    }

    @Test
    fun createPermission(){
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns uuid

        Given {
            contentType("application/json")
            body(requestBody)
        } When {
            post("/roles")
        } Then {
            statusCode(201)
        } Extract {
            val responseBody = JsonParser.parseString(body().asString())
            val expectedBody = JsonParser.parseString(expectedJson)
            Assertions.assertThat(responseBody).isEqualTo(expectedBody)
        }
    }

}