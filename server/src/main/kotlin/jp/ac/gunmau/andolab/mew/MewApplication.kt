package jp.ac.gunmau.andolab.mew

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MewApplication

fun main(args: Array<String>) {
	runApplication<MewApplication>(*args)
}
