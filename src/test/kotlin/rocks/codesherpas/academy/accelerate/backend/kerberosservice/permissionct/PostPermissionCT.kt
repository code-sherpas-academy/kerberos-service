package rocks.codesherpas.academy.accelerate.backend.kerberosservice.permissionct

import com.google.gson.JsonParser
import io.restassured.RestAssured
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PostPermissionCT {

    @LocalServerPort
    val port:Int = 0
    val expectedJson = """
        {
            "data": {
                "id": null,
                "description": "This is a post permission"
            } 
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
        Given {
                contentType("application/json")
                body(requestBody)
        }When{
                post("/permissions")

        }Then{
            statusCode(201)

        }Extract{
            val responseBody = JsonParser.parseString(body().asString())
            val expectedBody = JsonParser.parseString(expectedJson)
            //assertThat(responseBody).isEqualTo(expectedBody)
        }
    }

}