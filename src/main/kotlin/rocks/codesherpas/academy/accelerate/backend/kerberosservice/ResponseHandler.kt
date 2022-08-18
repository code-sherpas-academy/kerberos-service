package rocks.codesherpas.academy.accelerate.backend.kerberosservice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class ResponseHandler {

    fun generateResponse(message: String, status: HttpStatus, responseObj: Any): ResponseEntity<Any?>? {
        val map: MutableMap<String, Any> = HashMap()
        map["message"] = message
        map["status"] = status.value()
        map["data"] = responseObj
        return ResponseEntity(map, status)
    }

}