package rocks.codesherpas.academy.accelerate.backend.kerberosservice

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.util.*

@RestController
class PermissionsController(private val permissionRepository: PermissionRepository, private val responseHandler:ResponseHandler ) {

    @GetMapping("/permissions")
    fun getAll(): ResponseEntity<Any?>? {
        var permission =  permissionRepository.findAll().map { permission -> PermissionResourceWithId(permission.id, permission.description) }

        return responseHandler.generateResponse("Permissions successfully retrieved", HttpStatus.OK,  permission)
    }

    @GetMapping("/permissions/{id}")
    fun getOne(@PathVariable("id") id: String): ResponseEntity<Any?>? {
        val searchedPermission = permissionRepository.findById(id)

        if (searchedPermission.isPresent) {
            val permission = searchedPermission.get()
            val retrievedPermission =  PermissionResourceWithId(permission.id, permission.description)
            return responseHandler.generateResponse("Permission successfully retrieved", HttpStatus.OK, retrievedPermission)
        } else {
           // throw ResponseStatusException(HttpStatus.NOT_FOUND, "no permission with id $id")
            return  responseHandler.generateResponse("No permission with id $id", HttpStatus.NOT_FOUND,"null")
        }
    }

    @PostMapping("/permissions")
    fun create(@RequestBody permissionResource: PermissionResource): ResponseEntity<Any?>? {
        val permission = Permission(UUID.randomUUID().toString(), permissionResource.description)
        val savedPermission = permissionRepository.save(permission)

        val permissionResourceWithId =  PermissionResourceWithId(savedPermission.id, savedPermission.description)
        return responseHandler.generateResponse("Permission created successfully", HttpStatus.CREATED, permissionResourceWithId)
    }

    @PutMapping("/permissions/{id}")
    fun update(@RequestBody permissionResource: PermissionResource, @PathVariable("id") id: String): ResponseEntity<Any?>? {
        val permission = permissionRepository.findById(id)
        if (permission.isPresent) {
            val updatedPermission = Permission(id, permissionResource.description)
            val savedPermission = permissionRepository.save(updatedPermission)
            val permissionResourceWithId  = PermissionResourceWithId(savedPermission.id, savedPermission.description)

            return responseHandler.generateResponse("Permission with id: $id updated successfully", HttpStatus.OK, permissionResourceWithId)
        } else {
            return  responseHandler.generateResponse("No permission with id $id", HttpStatus.NOT_FOUND,"null")

        }
    }

    @DeleteMapping("/permissions/{id}")
    fun delete(@PathVariable("id") id: String):ResponseEntity<Any?>?{
        val permission = permissionRepository.findById(id)
        if(permission.isPresent){
           var result =  permissionRepository.deleteById(id)
            return responseHandler.generateResponse("Permission Deleted Successfully", HttpStatus.OK, result )
        }else{
            return  responseHandler.generateResponse("No permission with id $id", HttpStatus.NOT_FOUND,"null")

        }
    }
}