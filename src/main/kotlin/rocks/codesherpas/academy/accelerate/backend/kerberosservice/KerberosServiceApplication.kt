package rocks.codesherpas.academy.accelerate.backend.kerberosservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class KerberosServiceApplication

fun main(args: Array<String>) {
	runApplication<KerberosServiceApplication>(*args)
}
