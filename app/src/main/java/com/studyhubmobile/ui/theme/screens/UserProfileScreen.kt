package com.studyhubmobile.ui.theme.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonOff
import androidx.compose.material3.*
import androidx.compose.material3.MenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.studyhubmobile.R
import com.studyhubmobile.models.User
import com.studyhubmobile.models.UserStats
import androidx.compose.foundation.clickable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserProfileScreen(
    user: User? = null,
    navController: NavController,
    onLogout: () -> Unit,
    onUpdateProfile: (User) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    val menuItems = listOf(
        "Home" to { navController.navigate("home") },
        "Recursos" to { navController.navigate("recursos") },
        "Simulacro" to { navController.navigate("simulacro") },
        "Perfil" to { navController.navigate("perfil") }
    ) as List<Pair<String, () -> Unit>>

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0f172a))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { showMenu = true }
                        ) {
                            Icon(Icons.Default.Menu, contentDescription = "Menú", tint = Color.White)
                        }
                        Text(
                            text = "StudyHub",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier
                            .width(200.dp)
                            .background(Color(0xFF0f172a))
                    ) {
                        menuItems.forEach { (text, action) ->
                            DropdownMenuItem(
                                text = { Text(text, color = Color.White) },
                                onClick = {
                                    action()
                                    showMenu = false
                                }
                            )
                        }
                    }
                }
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(120.dp)
                                .padding(vertical = 8.dp),
                            tint = Color(0xFF3B82F6)
                        )
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = user?.nombre ?: "Usuario",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = user?.email ?: "",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                text = "Semestre: 1",
                                fontSize = 16.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        IconButton(
                            onClick = { user?.let { onUpdateProfile(it) } }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Editar perfil",
                                tint = Color.White
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Estadísticas",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        StatItem(
                            icon = Icons.Default.CheckCircle,
                            title = "Exámenes",
                            value = "0"
                        )
                        StatItem(
                            icon = Icons.Default.CheckCircle,
                            title = "Correctas",
                            value = "0"
                        )
                        StatItem(
                            icon = Icons.Default.CheckCircle,
                            title = "Promedio",
                            value = "0.0"
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Opciones",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OptionItem(
                            icon = Icons.Default.Edit,
                            title = "Editar perfil",
                            onClick = { user?.let { onUpdateProfile(it) } }
                        )
                        OptionItem(
                            icon = Icons.Default.Lock,
                            title = "Cambiar contraseña",
                            onClick = { /* Lógica para cambiar contraseña */ }
                        )
                        OptionItem(
                            icon = Icons.Default.Notifications,
                            title = "Notificaciones",
                            onClick = { /* Lógica para notificaciones */ }
                        )
                        OptionItem(
                            icon = Icons.Default.PersonOff,
                            title = "Cerrar sesión",
                            onClick = onLogout
                        )
                    }
                }
            }
        }
    )


}

@Composable
private fun StatItem(icon: ImageVector, title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color.White
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
    }
}

@Composable
private fun OptionItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.White
            )
            Text(
                text = title,
                color = Color.White
            )
        }
    }
}