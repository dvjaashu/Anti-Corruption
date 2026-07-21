package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val verificationId: String,
    val passwordHash: String,
    val role: String,
    val timestamp: Long
)
