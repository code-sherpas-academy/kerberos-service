package rocks.codesherpas.academy.accelerate.backend.kerberosservice

import javax.persistence.Entity
import javax.persistence.Id

@Entity
class Permission(@Id val id: String, val description: String) {

}
