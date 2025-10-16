package com.codebyallan.todo_list.service

import com.codebyallan.todo_list.dto.CreateTaskDto
import com.codebyallan.todo_list.dto.UpdateTaskDto
import com.codebyallan.todo_list.entity.Task
import com.codebyallan.todo_list.mapper.applyTo
import com.codebyallan.todo_list.mapper.toEntity
import com.codebyallan.todo_list.repository.TaskRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

/**
 * Service layer responsible for business logic, data validation, and transaction
 * management for the [Task] entity.
 *
 * @property repository The data access object JPA repository for Task entities.
 */
@Service
class TaskService(private val repository: TaskRepository) {
    /**
     * Creates a new [Task] entity based on the provided DTO and persists it to the database.
     *
     * @param createTaskDto The data transfer object containing the necessary data for creation.
     * @return The newly created [Task] entity, including its generated ID and timestamps.
     */
    fun createTask(createTaskDto: CreateTaskDto): Task {
        val taskEntity = createTaskDto.toEntity()
        return repository.save<Task>(taskEntity)
    }

    /**
     * Retrieves all existing [Task] entities from the database.
     *
     * @return A [List] of all tasks.
     */
    fun getAllTasks(): List<Task> {
        return repository.findAll().toList()
    }

    /**
     * Retrieves a single [Task] entity by its unique identifier (ID).
     *
     * @param id The ID of the task to retrieve.
     * @return The found [Task] entity.
     * @throws ResponseStatusException if no task is found with the given ID (HTTP 404 NOT FOUND).
     */
    fun getTaskById(id: Long): Task {
        return repository.findById(id).orElseThrow {
            ResponseStatusException(
                HttpStatus.NOT_FOUND, "Task not found with ID: $id"
            )
        }
    }

    /**
     * Updates an existing [Task] entity with the data provided in the DTO.
     *
     * Only non-null fields in the DTO will be applied to the entity.
     *
     * @param id The ID of the task to update.
     * @param updateTaskDto The data transfer object containing the fields to update.
     * @return The updated [Task] entity.
     * @throws ResponseStatusException if a provided title is blank (HTTP 400 BAD REQUEST).
     */
    fun updateTask(id: Long, updateTaskDto: UpdateTaskDto): Task {
        val existingTask = getTaskById(id)
        if (updateTaskDto.title != null && updateTaskDto.title.isBlank()) {
            throw ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Title cannot be empty or just white spaces when provided."
            )
        }
        updateTaskDto.applyTo(existingTask)


        return repository.save<Task>(existingTask)
    }

    /**
     * Deletes a [Task] entity by its unique identifier (ID).
     *
     * @param id The ID of the task to delete.
     * @throws ResponseStatusException if no task is found with the given ID (HTTP 404 NOT FOUND).
     */
    fun deleteTask(id: Long) {
        val existingTask = getTaskById(id)
        repository.delete(existingTask)

    }

}