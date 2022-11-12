package com.example.stronk.network.dtos

import kotlinx.serialization.Serializable

@Serializable
data class User(val id: Int, val username: String, val gender: String, val avatarUrl: String, val date: Long)

@Serializable
data class Routine(val id: Int, val name: String, val description: String, val creationDate: Long, val rating: Int, val difficulty: String, val user: User, val category: String)

@Serializable
data class PaginatedRoutines(val totalCount: Int, val orderBy: String, val direction: String, val content: List<Routine>, val size: Int, val page: Int, val isLastPage: Boolean)