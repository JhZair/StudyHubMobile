package com.studyhubmobile.models

data class UserStats(
    val totalExams: Int = 0,
    val correctAnswers: Int = 0,
    val totalQuestions: Int = 0,
    val averageScore: Float = 0f
)

data class UserProfile(
    val name: String,
    val email: String,
    val currentSemester: Int,
    val stats: UserStats = UserStats()
)

fun UserProfile.copy(
    name: String = this.name,
    email: String = this.email,
    currentSemester: Int = this.currentSemester,
    stats: UserStats = this.stats
) = UserProfile(name, email, currentSemester, stats)
