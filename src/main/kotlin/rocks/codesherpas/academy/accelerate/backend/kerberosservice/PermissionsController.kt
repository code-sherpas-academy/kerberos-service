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
import java.util.*

@RestController
class PermissionsController(
    private val permissionRepository: PermissionRepository,
    private val responseHandler:ResponseHandler
) {

    @GetMapping("/permissions")
    fun getAll(): ResponseEntity<Any> {
        val permissions = permissionRepository.findAll()
            .map { permission -> PermissionResourceWithId(permission.id, permission.description) }

        return responseHandler
            .generateResponse("Permissions successfully retrieved", HttpStatus.OK, permissions)
    }

    @GetMapping("/permissions/{id}")
    fun getOne(@PathVariable("id") id: String): ResponseEntity<Any> {
        val searchedPermission = permissionRepository.findById(id)

        return if (searchedPermission.isPresent) {
            val foundPermission = searchedPermission.get()
            val permissionToBeReturned = PermissionResourceWithId(foundPermission.id, foundPermission.description)
            responseHandler
                .generateResponse("Permission successfully retrieved", HttpStatus.OK, permissionToBeReturned)
        } else {
            responseHandler.generateResponse("No permission with id: $id", HttpStatus.NOT_FOUND)
        }
    }

    @PostMapping("/permissions")
    fun create(@RequestBody permissionResource: PermissionResource): ResponseEntity<Any> {
        val createdPermission = Permission(UUID.randomUUID().toString(), permissionResource.description)
        val savedPermission = permissionRepository.save(createdPermission)
        val permissionToBeReturned = PermissionResourceWithId(savedPermission.id, savedPermission.description)

        return responseHandler
            .generateResponse("Permission created successfully", HttpStatus.CREATED, permissionToBeReturned)
    }

    @PutMapping("/permissions/{id}")
    fun update(
        @RequestBody permissionResource: PermissionResource,
        @PathVariable("id") id: String
    ): ResponseEntity<Any> {
        val permissionToBeUpdated = permissionRepository.findById(id)

        return if (permissionToBeUpdated.isPresent) {
            val updatedPermission = Permission(id, permissionResource.description)
            val savedPermission = permissionRepository.save(updatedPermission)
            val permissionToBeReturned = PermissionResourceWithId(savedPermission.id, savedPermission.description)

            responseHandler
                .generateResponse("Permission with id: $id updated successfully", HttpStatus.OK, permissionToBeReturned)
        } else {
            responseHandler.generateResponse("No permission with id: $id", HttpStatus.NOT_FOUND)
        }
    }

    @DeleteMapping("/permissions/{id}")
    fun delete(@PathVariable("id") id: String): ResponseEntity<Any> {
        val permissionToBeDeleted = permissionRepository.findById(id)

        return if (permissionToBeDeleted.isPresent){
            permissionRepository.deleteById(id)
            responseHandler.generateResponse("Permission with id: $id deleted successfully", HttpStatus.OK)
        } else {
            responseHandler.generateResponse("No permission with id: $id", HttpStatus.NOT_FOUND)
        }
    }
}