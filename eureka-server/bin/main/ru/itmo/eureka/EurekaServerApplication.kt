package ru.itmo.market

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@EnableEurekaServer  // ✅ Keep this (Eureka Server)
@ComponentScan(basePackages = ["ru.itmo.market"])
class EurekaServerApplication

fun main(args: Array<String>) {
    runApplication<EurekaServerApplication>(*args)
}
