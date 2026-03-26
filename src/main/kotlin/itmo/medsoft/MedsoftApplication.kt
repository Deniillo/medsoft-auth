package itmo.medsoft

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["itmo.medsoft"])
class MedsoftApplication

fun main(args: Array<String>) {
	runApplication<MedsoftApplication>(*args)
}
