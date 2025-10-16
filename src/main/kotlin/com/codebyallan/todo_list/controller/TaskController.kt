package com.codebyallan.todo_list.controller

import com.codebyallan.todo_list.dto.CreateTaskDto
import com.codebyallan.todo_list.dto.UpdateTaskDto
import com.codebyallan.todo_list.entity.Task
import com.codebyallan.todo_list.service.TaskService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * Manages all REST operations for Task resources.
 * Maps HTTP requests to service methods.
 */
@RestController
@RequestMapping("/tasks")
class TaskController(private val service: TaskService) {
    /**
     * Create New Task.
     * @param createTaskDto the input dto containing the data for the new task.
     * @return [ResponseEntity] with task created and status code 201.
     */
    @PostMapping()
    fun create(@RequestBody @Valid createTaskDto: CreateTaskDto): ResponseEntity<Task> {
        val task = service.createTask(createTaskDto)
        return ResponseEntity.status(HttpStatus.CREATED).body(task)
    }

    /**
     * Get All Tasks.
     * @return [ResponseEntity] with List of [Task] and Status Code 200.
     */
    @GetMapping()
    fun findAll(): ResponseEntity<List<Task>> {
        val tasks = service.getAllTasks()
        return ResponseEntity.ok(tasks)
    }

    /**
     * Retrieves a specific task by its ID.
     *
     * @param id The ID Long of the task to be fetched.
     * @return [ResponseEntity] with [Task] and Status Code 200 OK or 404 Not Found.
     */
    @GetMapping("/{id}")
    fun find(@PathVariable id: Long): ResponseEntity<Task> {
        val task = service.getTaskById(id)
        return ResponseEntity.ok(task)
    }

    /**
     * Update Task by its ID.
     * @param id The ID Long of the task to be updated.
     * @param updateTaskDto the input dto containing the data for the updated task.
     * @return [ResponseEntity] with [Task] and Status Code 200 OK or 404 Not Found.
     */

    @PatchMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody @Valid updateTaskDto: UpdateTaskDto): ResponseEntity<Task> {
        val updatedTask = service.updateTask(id, updateTaskDto)
        return ResponseEntity.ok(updatedTask)
    }

    /**
     * Deletes a task by its ID.
     * @param id The ID Long of the task to be deleted.
     * @return [ResponseEntity] with Status Code 204 No Content (or 404 Not Found if the resource does not exist).
     */
    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Long): ResponseEntity<Unit> {
        service.deleteTask(id)
        return ResponseEntity.noContent().build()
    }
}