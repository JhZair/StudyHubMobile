package com.studyhubmobile.ui.theme.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.MenuDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.navigation.NavController
import com.studyhubmobile.models.Question
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimulacroScreen(navController: NavController) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val menuItems = listOf(
        "Home" to { navController.navigate("home") },
        "Recursos" to { navController.navigate("recursos") },
        "Simulacro" to { navController.navigate("simulacro") },
        "Perfil" to { navController.navigate("perfil") }
    )

    var expandedSemester by remember { mutableStateOf<Int?>(null) }
    var selectedCourse by remember { mutableStateOf<String?>(null) }

    val coursesBySemester = mapOf(
        1 to listOf(
            "matematicasi",
            "introduccion-a-la-vida-universitaria",
            "comunicacion",
            "estructuras-discretas-i",
            "programacion-de-videojuegos",
            "metodologia-del-estudio"
        ),
        2 to listOf("matematicas-ii", "fisica-ii", "quimica-ii"),
        3 to listOf("matematicas-iii", "fisica-iii", "quimica-iii"),
        4 to listOf("matematicas-iv", "fisica-iv", "quimica-iv"),
        5 to listOf("matematicas-v", "fisica-v", "quimica-v"),
        6 to listOf("matematicas-vi", "fisica-vi", "quimica-vi"),
        7 to listOf("matematicas-vii", "fisica-vii", "quimica-vii"),
        8 to listOf("matematicas-viii", "fisica-viii", "quimica-viii"),
        9 to listOf("matematicas-ix", "fisica-ix", "quimica-ix"),
        10 to listOf("matematicas-x", "fisica-x", "quimica-x")
    )

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
                            onClick = { isMenuExpanded = !isMenuExpanded }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menú",
                                tint = Color.White
                            )
                        }
                        Text(
                            text = "StudyHub",
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                }
                
                DropdownMenu(
                    expanded = isMenuExpanded,
                    onDismissRequest = { isMenuExpanded = false },
                    modifier = Modifier
                        .width(200.dp)
                        .background(Color(0xFF0f172a))
                ) {
                    menuItems.forEach { (text, action) ->
                        DropdownMenuItem(
                            text = { Text(text, color = Color.White) },
                            onClick = {
                                action()
                                isMenuExpanded = false
                            }
                        )
                    }
                }
            }
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .padding(top = 48.dp)
                    .padding(paddingValues = padding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (semester in 1..10) {
                        SemesterButton(
                            semester = semester,
                            expanded = expandedSemester == semester,
                            onClick = {
                                expandedSemester = if (expandedSemester == semester) null else semester
                            }
                        )
                        if (expandedSemester == semester) {
                            val courses = coursesBySemester[semester] ?: emptyList()
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp)
                            ) {
                                courses.forEach { course ->
                                    CourseButton(
                                        course = course,
                                        selected = selectedCourse == course,
                                        onClick = { courseName ->
                                            // Convertir el nombre del curso para que coincida con el nombre del archivo JSON
                                            val formattedCourseName = courseName
                                                .replace(" ", "")  // Remover espacios
                                                .replace("ñ", "n") // Remplazar ñ
                                                .replace("-", "-") // Mantener guiones
                                            navController.navigate("exam/$formattedCourseName")
                                            expandedSemester = null
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }
    )
}

@Composable
private fun SemesterButton(semester: Int, expanded: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFF0f172a), RoundedCornerShape(6.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            val romanSemester = toRoman(semester)
            Text(
                text = "SEMESTRE $romanSemester",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = Icons.Default.KeyboardArrowDown,
                contentDescription = "Expandir",
                tint = Color.White,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}

@Composable
private fun CourseButton(course: String, selected: Boolean, onClick: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color(0xFF0F172A), RoundedCornerShape(6.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick(course)
            },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = course,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
            if (selected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Seleccionado",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
        }
    }
}

private fun toRoman(number: Int): String {
    val romans = listOf("I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X")
    return if (number in 1..10) romans[number - 1] else number.toString()
}