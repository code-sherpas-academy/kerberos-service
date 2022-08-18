package rocks.codesherpas.academy.accelerate.backend.kerberosservice.role

import rocks.codesherpas.academy.accelerate.backend.kerberosservice.permission.PermissionResourceWithId

class RoleResourceWithId(id: String, val description: String, val permissions: List<PermissionResourceWithId>) {

}
