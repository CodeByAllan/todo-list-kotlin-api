package com.codebyallan.todo_list.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * Data transfer object to updated tasks.
 */
data class UpdateTaskDto(
    /**
     * Title is optional.
     * Must have a maximum of 100 characters.
     */
    @field:Size(
        max = 100, message = "Title must have a maximum of 100 characters!"
    ) val title: String?,
    /**
     * Description is optional.
     * But when passed it must have a maximum of 500 characters.
     */
    @field:Size(max = 500, message = "Description must have a maximum of 500 characters!") val description: String?,
    /**
     * IsDone is optional.
     */
    val isDone: Boolean?
)