package com.studyhubmobile.session

import com.studyhubmobile.models.User
import com.studyhubmobile.models.UserStats

object SessionManager {
    var currentUser: User? = null
    var userStats: UserStats = UserStats()

    fun logout() {
        currentUser = null
        userStats = UserStats()
    }
}
