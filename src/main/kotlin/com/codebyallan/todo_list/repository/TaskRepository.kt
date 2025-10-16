package com.codebyallan.todo_list.repository

import com.codebyallan.todo_list.entity.Task
import org.springframework.data.repository.CrudRepository

/**
 * Repository interface for managing [Task] entities.
 *
 * Extends [CrudRepository] to provide basic CRUD operations (Create, Read, Update, Delete)
 * without requiring manual implementation.
 */

interface TaskRepository : CrudRepository<Task, Long> {}