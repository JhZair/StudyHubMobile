package com.studyhubmobile.session

import com.studyhubmobile.models.User

object SessionManager {
    var currentUser: User? = null

    fun logout() {
        currentUser = null
    }
}
