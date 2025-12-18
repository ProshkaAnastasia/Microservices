package ru.itmo.market

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.config.server.EnableConfigServer
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EnableConfigServer
@ComponentScan(basePackages = ["ru.itmo.market"])
class ConfigServerApplication

fun main(args: Array<String>) {
    runApplication<ConfigServerApplication>(*args)
}
