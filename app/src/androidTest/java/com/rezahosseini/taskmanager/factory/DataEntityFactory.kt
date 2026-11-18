
package com.rezahosseini.taskmanager.factory

import com.rezahosseini.taskmanager.model.local.DataEntity

object DataEntityFactory {

    fun create(
        id: Long = 0L,
        name: String = "Test Task",
        about: String = "Test Description",
        done: Boolean = false,
        priority: Int = 1
    ) = DataEntity(
        id = id,
        name = name,
        about = about,
        done = done,
        priority = priority
    )
}
