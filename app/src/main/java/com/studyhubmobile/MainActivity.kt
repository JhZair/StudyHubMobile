package com.studyhubmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.studyhubmobile.ui.theme.screens.ExamScreen
import com.studyhubmobile.ui.theme.screens.TriviaExamScreen
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import com.studyhubmobile.ui.theme.StudyHubMobileTheme
import com.studyhubmobile.ui.theme.screens.HomeScreen
import com.studyhubmobile.ui.theme.screens.SimulacroScreen
import com.studyhubmobile.ui.theme.screens.RecursosScreen
import com.studyhubmobile.ui.theme.screens.LoginScreen
import com.studyhubmobile.ui.theme.screens.SignUpScreen
import com.studyhubmobile.ui.theme.screens.LoadingScreen
import com.studyhubmobile.ui.theme.screens.UserProfileScreen
import com.studyhubmobile.models.UserProfile
import com.studyhubmobile.models.UserStats
import com.studyhubmobile.session.SessionManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudyHubMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isLoading by remember { mutableStateOf(true) }
                    
                    if (isLoading) {
                        LoadingScreen(
                            navController = rememberNavController(),
                            onLoadingComplete = { isLoading = false }
                        )
                    } else {
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)
                        ) {
                            AppNavigation()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var currentUser by remember { mutableStateOf(SessionManager.currentUser) }

    LaunchedEffect(Unit) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            currentUser = SessionManager.currentUser
        }
    }
    
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable(
            "home",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) {
            HomeScreen(
                navController = navController
            )
        }
        composable(
            "login",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) { LoginScreen(navController) }
        composable(
            "register",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) { SignUpScreen(navController) }
        composable(
            "simulacro",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) { SimulacroScreen(navController) }
        composable(
            "recursos",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) { RecursosScreen(navController) }
        composable(
            "perfil",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) {
            if (currentUser != null) {
                UserProfileScreen(
                    user = com.studyhubmobile.models.UserProfile(
                        name = currentUser!!.nombre,
                        email = currentUser!!.email,
                        currentSemester = 1
                    ),
                    navController = navController,
                    onLogout = { navController.navigate("login") },
                    onUpdateProfile = { /* No implementado */ }
                )
            } else {
                UserProfileScreen(
                    user = com.studyhubmobile.models.UserProfile(
                        name = "Usuario",
                        email = "usuario@email.com",
                        currentSemester = 1
                    ),
                    navController = navController,
                    onLogout = { navController.navigate("login") },
                    onUpdateProfile = { /* No implementado */ }
                )
            }
        }
        composable(
            "exam/trivia",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) {
            TriviaExamScreen(navController = navController)
        }
        composable(
            "exam/{course}",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
        ) { backStackEntry ->
            ExamScreen(
                navController = navController,
                route = backStackEntry.arguments?.getString("course") ?: ""
            )
        }
    }
}