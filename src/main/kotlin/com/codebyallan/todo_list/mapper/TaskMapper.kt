package com.codebyallan.todo_list.mapper

import com.codebyallan.todo_list.dto.CreateTaskDto
import com.codebyallan.todo_list.dto.UpdateTaskDto
import com.codebyallan.todo_list.entity.Task

/**
 * It is an extension of [CreateTaskDto] that maps this dto to the [Task] class model.
 * @return A New [Task] Instance
 */
fun CreateTaskDto.toEntity(): Task {
    return Task(
        title = this.title,
        description = this.description,
    )
}

/**
 * Applies non-null fields from the [UpdateTaskDto] to an existing [Task] entity.
 * This method ensures that fields are only updated if explicitly provided in the DTO.
 *
 * @param task The existing [Task] entity to be updated.
 * @return The updated [Task] entity instance.
 */
fun UpdateTaskDto.applyTo(task: Task): Task {
    this.title?.let { title -> task.title = title }
    this.description?.let { description -> task.description = description }
    this.isDone?.let { isDone -> task.isDone = isDone }
    return task
}