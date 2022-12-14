package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permissionct

import com.google.gson.JsonParser
import io.mockk.every
import io.mockk.mockkStatic
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
import java.util.UUID

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostPermissionCT {

    @LocalServerPort
    val port:Int = 0

    private final val uuid: UUID = UUID.randomUUID()

    val expectedJson = """
        {
            "id": "$uuid",
            "description": "This is a post permission"
        }
    """.trimIndent()

    var requestBody:String = """
        {
            "description":"This is a post permission"
        }
    """.trimIndent()

    @BeforeEach
    fun setUp(){
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @Test
    fun createPermission(){
        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns uuid

        Given {
            contentType("application/json")
            body(requestBody)
        } When {
            post("/permissions")
        } Then {
            statusCode(201)
        } Extract {
            val responseBody = JsonParser.parseString(body().asString())
            val expectedBody = JsonParser.parseString(expectedJson)
            assertThat(responseBody).isEqualTo(expectedBody)
        }
    }
}