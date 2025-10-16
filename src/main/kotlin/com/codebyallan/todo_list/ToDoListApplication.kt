package com.codebyallan.todo_list

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Application Entry Point.
 *  Configures Spring and Initializes REST API Services.
 */
@SpringBootApplication
class ToDoListApplication

fun main(args: Array<String>) {
    runApplication<ToDoListApplication>(*args)
}
