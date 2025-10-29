package com.codebyallan.todo_list.mapper

import com.codebyallan.todo_list.dto.CreateTaskDto
import com.codebyallan.todo_list.dto.UpdateTaskDto
import com.codebyallan.todo_list.entity.Task
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNull
import kotlin.test.assertEquals

class TaskMapperTest {
    private val title = "play soccer in Pc"
    private val description = "EA SPORTS FC 24"

    @Test
    fun `Check if the CreateTaskDto extension method maps correctly to Task`() {
        val createTaskDto = CreateTaskDto(title = title, description = description)
        val task = createTaskDto.toEntity()
        assertInstanceOf(Task::class.java, task, "Checks if what is returned by the Dto method is a task.")
        assertNull(task.id, "Checks if the id is null in the pre save")
        assertEquals(title, task.title, "Checks if the title was correctly mapped from the DTO.")
        assertEquals(
            description, task.description, "Checks if the description was correctly mapped from the DTO."
        )
        assertEquals(task.isDone, false, "Checks if the default attribute is false")
    }

    @Test
    fun `Update the Task using UpdateTaskDto without changing any attributes that are passed as null and without altering the id`() {
        val updateTitle = "play soccer in Pc"
        val updateDescription = "EA SPORTS FC 25"
        val task = Task(id = 1, title = title, description = description)
        val updateTaskDto = UpdateTaskDto(title = updateTitle, description = updateDescription, isDone = null)
        val result = updateTaskDto.applyTo(task)
        assertEquals(task.id, result.id, "The ID before and after must remain the same.")
        assertEquals(result.title, updateTitle, "The title should be updated successfully.")
        assertEquals(result.description, updateDescription, "The description should be updated successfully.")
        assertEquals(
            false, result.isDone, "isDone should not be changed because the attribute was passed as null in the update."
        )
    }

}