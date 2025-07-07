package com.studyhubmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.navigation.NavHost
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.studyhubmobile.ui.theme.screens.*
import com.studyhubmobile.models.User
import com.studyhubmobile.data.repository.AuthRepository
import com.studyhubmobile.ui.theme.StudyHubMobileTheme

@Composable
fun LoadingScreen(
    navController: NavHostController,
    onLoadingComplete: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(50.dp)
                .align(Alignment.Center)
        )
    }
    LaunchedEffect(Unit) {
        delay(1000)
        onLoadingComplete()
    }
}

class MainActivity : ComponentActivity() {
    val authRepository = AuthRepository()
    

    

    
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StudyHubMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    AppNavigation(
                        navController = navController,
                        currentUser = authRepository.currentUser
                    )
                }
            }
        }
    }
}

@Composable
fun AppNavigation(
    navController: NavHostController,
    currentUser: User?
) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
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
        ) { backStackEntry ->
            LoginScreen(
                navController = navController,
                onLogin = { user: User? ->
                    if (user != null) {
                        (navController.context as MainActivity).authRepository.currentUser = user
                        navController.navigate("home") { 
                            popUpTo("login") { 
                                inclusive = true
                            }
                        }
                    } else {
                        (navController.context as MainActivity).authRepository.currentUser = null
                    }
                }
            )
        }

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
        ) { backStackEntry ->
            HomeScreen(
                navController = navController,
                currentUser = (navController.context as MainActivity).authRepository.currentUser
            )
        }



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
        ) { backStackEntry ->
            RegisterScreen(
                navController = navController,
                onRegister = { user: User? ->
                    if (user != null) {
                        (navController.context as MainActivity).authRepository.currentUser = user
                        navController.navigate("home") {
                            popUpTo("register") {
                                inclusive = true
                            }
                        }
                    }
                }
            )
        }

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
        ) { backStackEntry ->
            SimulacroScreen(navController)
        }

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
        ) { backStackEntry ->
            RecursosScreen(navController)
        }

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
        ) { backStackEntry ->
            if (currentUser != null) {
                UserProfileScreen(
                    user = currentUser!!,
                    navController = navController,
                    onLogout = {
                        (navController.context as MainActivity).authRepository.currentUser = null
                        navController.navigate("login") {
                            popUpTo("perfil") { inclusive = true }
                        }
                    },
                    onUpdateProfile = { updatedUser ->
                        (navController.context as MainActivity).authRepository.currentUser = updatedUser
                    }
                )
            } else {
                Text(
                    text = "No hay usuario autenticado",
                    style = MaterialTheme.typography.bodyLarge
                )
                Button(
                    onClick = {
                        navController.navigate("login")
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Iniciar Sesión")
                }
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
        ) { backStackEntry ->
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