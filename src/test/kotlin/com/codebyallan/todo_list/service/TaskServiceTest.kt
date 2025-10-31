package com.codebyallan.todo_list.service

import com.codebyallan.todo_list.dto.CreateTaskDto
import com.codebyallan.todo_list.dto.UpdateTaskDto
import com.codebyallan.todo_list.entity.Task
import com.codebyallan.todo_list.repository.TaskRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.web.server.ResponseStatusException
import java.util.Optional

class TaskServiceTest {
    private val repository: TaskRepository = mockk()
    private val service: TaskService = TaskService(repository)

    private val newTaskDto = CreateTaskDto(title = "play soccer in Pc", description = "EA SPORTS FC 24")
    val task = Task(id = 1, title = newTaskDto.title, description = newTaskDto.description)

    @Test
    fun `createTask should throw an execution when failing to create task`() {
        every {
            repository.save(any())
        } throws DataIntegrityViolationException("database failure")
        assertThrows(DataIntegrityViolationException::class.java) {
            service.createTask(newTaskDto)
        }
        verify(exactly = 1) {
            repository.save(any())
        }
    }

    @Test
    fun `createTask you must create a new task and return it`() {
        every { repository.save<Task>(any()) } returns task
        val result = service.createTask(newTaskDto)
        assertEquals(1, result.id, "The returned id must be equal to 1")
        val taskSlot = slot<Task>()
        verify(exactly = 1) { repository.save<Task>(capture(taskSlot)) }
        assertNull(taskSlot.captured.id, "The Entity sent to save() MUST have a null ID (pre-save).")
        assertEquals(newTaskDto.title, taskSlot.captured.title, "The DTO was correctly converted.")
    }

    @Test
    fun `getAllTasks should return an Empty List when there is no data`() {
        val tasks: List<Task> = listOf()
        every { repository.findAll() } returns tasks
        val result = service.getAllTasks()
        assertEquals(0, result.size, "Should return an empty list")
        verify(exactly = 1) { repository.findAll() }
    }

    @Test
    fun `getAllTasks should return all tasks when there is data`() {
        val tasks: List<Task> = listOf(
            task, Task(id = 2, title = "play soccer with best friend", description = "...")
        )
        every { repository.findAll() } returns tasks
        val result = service.getAllTasks()
        assertEquals(2, result.size, "Must return 2 tasks")
        verify(exactly = 1) { repository.findAll() }
    }

    @Test
    fun `getById should throw an exception when not finding the given task`() {
        val id: Long = 99
        every { repository.findById(id) } returns Optional.empty()
        val exception = assertThrows(ResponseStatusException::class.java) {
            service.getTaskById(id)
        }
        assertEquals(404, exception.statusCode.value())
        assertTrue(exception.reason!!.contains("Task not found with ID: 99"))
        verify(exactly = 1) {
            repository.findById(id)
        }
    }

    @Test
    fun `getById should return tasks by Id when there is data `() {
        val id: Long = 1
        every { repository.findById(id) } returns Optional.of(task)
        val result = service.getTaskById(id)
        assertEquals(id, result.id, "Must return id = 1")
        assertEquals(task.title, result.title)
        verify(exactly = 1) { repository.findById(any()) }
    }

    @Test
    fun `updateTask should throw an exception if the Task Id is not found`() {
        val id: Long = 1
        val updateDto = UpdateTaskDto(title = "play soccer in PS5", description = "EA SPORTS FC 25", isDone = true)

        every { repository.findById(id) } returns Optional.empty()

        val exception = assertThrows(ResponseStatusException::class.java) {
            service.updateTask(id, updateDto)
        }

        assertEquals(404, exception.statusCode.value())

        verify(exactly = 1) { repository.findById(id) }
        verify(exactly = 0) { repository.save(any()) }
    }

    @Test
    fun `updateTask should throw 400 when provided title is blank`() {
        val id: Long = 1
        val invalidDto = UpdateTaskDto(title = "   ", description = "EA SPORTS FC 25", isDone = true)

        every { repository.findById(id) } returns Optional.of(task)

        val exception = assertThrows(ResponseStatusException::class.java) {
            service.updateTask(id, invalidDto)
        }

        assertEquals(400, exception.statusCode.value())
        assertTrue(exception.reason!!.contains("Title cannot be empty"))

        verify(exactly = 0) { repository.save(any()) }
    }

    @Test
    fun `update you must update the fields and return the Task with the Id preserved`() {
        val id: Long = 1
        val updateTaskDto = UpdateTaskDto(title = "play soccer in PS5", description = "EA SPORTS FC 25", isDone = false)

        every { repository.findById(id) } returns Optional.of(task)
        every { repository.save(any()) } answers {
            val capturedTask = it.invocation.args[0] as Task
            capturedTask.copy(id = id)
        }

        val result = service.updateTask(id, updateTaskDto)

        assertEquals(id, result.id, "The ID must be preserved.")
        assertEquals(updateTaskDto.title, result.title, "The Title needs to be updated.")
        assertEquals(updateTaskDto.description, result.description, "The Description needs to be updated.")
        assertEquals(updateTaskDto.isDone, result.isDone, "The isDone status needs to be updated.")

        verify(exactly = 1) { repository.findById(id) }
        verify(exactly = 1) { repository.save(any()) }
    }

    @Test
    fun `deleteTask should call delete method for an existing Id`() {
        val id: Long = 1
        every { repository.findById(id) } returns Optional.of(task)
        every { repository.delete(task) } answers { Unit }

        service.deleteTask(id)

        verify(exactly = 1) { repository.delete(task) }
    }
}