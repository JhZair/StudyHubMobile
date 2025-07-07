package com.studyhubmobile.ui.theme.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.foundation.text.ClickableText
import androidx.navigation.NavController
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.studyhubmobile.R
import com.studyhubmobile.data.api.RecursoNetworkModule
import com.studyhubmobile.data.network.ApiService
import kotlinx.coroutines.launch
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.rememberCoroutineScope
import com.studyhubmobile.ui.components.ResourceCardComponent
import com.studyhubmobile.models.Recurso
import com.studyhubmobile.data.repository.RecursoRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import android.net.Uri
import android.content.Context
import android.widget.Toast

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecursosScreen(navController: NavController) {
    var isMenuExpanded by remember { mutableStateOf(false) }
    val menuItems = listOf(
        "Home" to { navController.navigate("home") },
        "Recursos" to { navController.navigate("recursos") },
        "Simulacro" to { navController.navigate("simulacro") },
        "Perfil" to { navController.navigate("perfil") }
    )
    val scope = rememberCoroutineScope()
    var recursos by remember { mutableStateOf<List<Recurso>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isUploadDialogOpen by remember { mutableStateOf(false) }
    var selectedFileUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    
    // Para seleccionar archivos
    val filePickerLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedFileUri = uri
    }

    val recursoRepository = remember { RecursoRepository(RecursoNetworkModule.apiService) }
    
    LaunchedEffect(Unit) {
        isLoading = true
        try {
            recursoRepository.getRecursos().collect { data ->
                recursos = data
            }
        } catch (e: Exception) {
            println("Error al cargar recursos: ${e.message}")
        } finally {
            isLoading = false
        }
    }

    // Función para seleccionar archivo
    fun selectFile() {
        filePickerLauncher.launch("*/*")
    }

    // Función para subir recurso
    fun uploadResource(titulo: String, descripcion: String, tipo: String, idCurso: String) {
        scope.launch {
            try {
                // Crear los RequestBody para los campos de texto
                val tituloBody = titulo.toRequestBody("text/plain".toMediaTypeOrNull())
                val descripcionBody = descripcion.toRequestBody("text/plain".toMediaTypeOrNull())
                val tipoBody = tipo.toRequestBody("text/plain".toMediaTypeOrNull())
                val idCursoBody = idCurso.toRequestBody("text/plain".toMediaTypeOrNull())

                // Abrir el archivo seleccionado
                val inputStream = context.contentResolver.openInputStream(selectedFileUri!!)
                val fileBody = inputStream?.let {
                    it.readBytes().toRequestBody(
                        context.contentResolver.getType(selectedFileUri!!)?.toMediaTypeOrNull()
                    )
                }

                // Crear el Part para el archivo
                val filePart = fileBody?.let {
                    MultipartBody.Part.createFormData(
                        "archivo",
                        selectedFileUri?.lastPathSegment ?: "archivo",
                        it
                    )
                }

                if (filePart != null) {
                    val response = ApiService.create().uploadResource(
                        titulo = tituloBody,
                        descripcion = descripcionBody,
                        tipo = tipoBody,
                        idCurso = idCursoBody,
                        archivo = filePart
                    )

                    if (response.isSuccessful) {
                        Toast.makeText(context, "Recurso subido exitosamente", Toast.LENGTH_SHORT).show()
                        isUploadDialogOpen = false
                        selectedFileUri = null
                        // Refrescar la lista de recursos
                        recursoRepository.getRecursos().collect { data ->
                            recursos = data
                        }
                    } else {
                        Toast.makeText(context, "Error al subir el recurso", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Error al leer el archivo", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Función para eliminar recurso
    fun deleteResource(recurso: Recurso) {
        scope.launch {
            try {
                val response = ApiService.create().deleteResource(recurso.idRecurso)
                if (response.isSuccessful) {
                    Toast.makeText(context, "Recurso eliminado exitosamente", Toast.LENGTH_SHORT).show()
                    // Refrescar la lista de recursos
                    recursoRepository.getRecursos().collect { data ->
                        recursos = data
                    }
                } else {
                    Toast.makeText(context, "Error al eliminar el recurso", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0f172a))
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

                IconButton(
                    onClick = { isUploadDialogOpen = true },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Agregar recurso",
                        tint = Color.White
                    )
                }
            }
        }
    ) { padding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0f172a))
                .padding(padding)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Recursos Disponibles",
                    color = Color.White,
                    fontSize = 22.sp,
                    modifier = Modifier.padding(bottom = 16.dp, top = 80.dp)
                )

                if (isLoading) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    if (recursos.isEmpty()) {
                        Text(
                            text = "No hay recursos disponibles.",
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp)
                        ) {
                            items(recursos) { recurso ->
                                ResourceCardComponent(
                                    title = recurso.titulo,
                                    description = recurso.descripcion,
                                    link = recurso.archivo,
                                    onEditClick = {
                                        // TODO: Implementar edición
                                    },
                                    onDeleteClick = { deleteResource(recurso) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Variables para el diálogo de subida
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf("") }
    var idCurso by remember { mutableStateOf("") }

    // Dialogo para subir recurso
    if (isUploadDialogOpen) {
        AlertDialog(
            onDismissRequest = { 
                isUploadDialogOpen = false
                titulo = ""
                descripcion = ""
                tipo = ""
                idCurso = ""
                selectedFileUri = null
            },
            title = { Text("Subir recurso") },
            text = {
                Column {
                    OutlinedTextField(
                        value = titulo,
                        onValueChange = { titulo = it },
                        label = { Text("Título") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = tipo,
                        onValueChange = { tipo = it },
                        label = { Text("Tipo") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = idCurso,
                        onValueChange = { idCurso = it },
                        label = { Text("ID del curso") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = { selectFile() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Seleccionar archivo")
                    }
                    
                    if (selectedFileUri != null) {
                        Text(
                            text = "Archivo seleccionado: ${selectedFileUri?.lastPathSegment}",
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { 
                        if (titulo.isEmpty() || descripcion.isEmpty() || tipo.isEmpty() || idCurso.isEmpty() || selectedFileUri == null) {
                            Toast.makeText(context, "Por favor, complete todos los campos y seleccione un archivo", Toast.LENGTH_SHORT).show()
                            return@Button
                        }

                        uploadResource(titulo, descripcion, tipo, idCurso)
                    }
                ) {
                    Text("Subir")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { isUploadDialogOpen = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}
