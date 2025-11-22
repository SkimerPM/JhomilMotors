package com.jhomilmotors.jhomilmotorsfff.ui.screens.DetailsProduct

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.data.model.product.ProductDetailsDto
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.home.ProductDetailViewModel

// ----------------------------------------------------------------------
// DTOs LOCALES Y FUNCIONES DE SOPORTE PARA LA PREVISUALIZACIÓN
// ----------------------------------------------------------------------

// NOTA: Mantenemos esta data class para que tus Previews sigan funcionando
data class Producto(
    val nombre: String,
    val precio: String,
    val descripcion: String,
    val urlCompartir: String
)

@Composable
fun ProductSection(title: String, content: @Composable (() -> Unit)? = null) {
    // [Tu código ProductSection para la sección de pestañas]
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { expanded = !expanded }
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = if (expanded) "Cerrar" else "Expandir",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.rotate(if (expanded) 180f else 0f)
                )
            }
        }
        if (content != null && expanded) {
            content()
            Spacer(modifier = Modifier.height(16.dp))
        }

        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}

fun shareProduct(context: Context, nombreProducto: String, urlProducto: String) {
    // [Tu código para compartir producto]
    val mensajeACompartir = "¡Mira qué producto encontré en Jhomil Motors! $nombreProducto. Cómpralo aquí: $urlProducto"

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain"
        putExtra(Intent.EXTRA_SUBJECT, "Producto Interesante en Jhomil Motors")
        putExtra(Intent.EXTRA_TEXT, mensajeACompartir)
    }

    context.startActivity(Intent.createChooser(shareIntent, "Compartir producto usando..."))
}

@Composable
fun DemoProduct(): Producto {
    // [Tu código DemoProduct]
    return Producto(
        nombre = "BATERIA ENERJET 11T56 56AH 405CCA TOYOTA 11PLC",
        precio = "S/320.00",
        descripcion = "La batería Enerjet 11T56 12V 56Ah 405CCA con 11 placas ha sido específicamente diseñada para satisfacer las necesidades energéticas de autos y camionetas Toyota.",
        urlCompartir = "https://jhomilmotors.com/bateria/enerjet-11t56"
    )
}

// ----------------------------------------------------------------------
// 2. COMPONENTES DE DISPLAY DINÁMICO
// ----------------------------------------------------------------------

// Componente: Carrusel de Imágenes
@Composable
fun ProductImageCarousel(images: List<ProductDetailsDto.ImagenResponse>) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 24.dp)
    ) {
        items(images, key = { it.id }) { image ->
            AsyncImage(
                model = image.url,
                contentDescription = "Imagen de producto",
                modifier = Modifier
                    .height(250.dp)
                    .width(300.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray),
                contentScale = ContentScale.Crop
            )
        }
    }
}

// Componente: Lista de Atributos (Ficha Técnica)
@Composable
fun ProductAttributesList(attributes: List<ProductDetailsDto.AtributoResponse>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        attributes.forEach { attr ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "${attr.nombre}:", fontSize = 14.sp, color = Color.DarkGray)
                val valor = attr.valorTexto ?: attr.valorNumerico?.toString() ?: "N/A"
                Text(text = "$valor ${attr.unidad ?: ""}", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onBackground)
            }
            Divider(color = Color.LightGray.copy(0.5f))
        }
    }
}

// Componente: Selector de Variantes (Chips)
@Composable
fun ProductVariantSelector(
    variantes: List<ProductDetailsDto.VarianteResponse>,
    selectedId: Long,
    onSelect: (Long) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(variantes, key = { it.id }) { variante ->
            FilterChip(
                selected = variante.id == selectedId,
                onClick = { onSelect(variante.id) },
                label = {
                    val variantName = variante.atributos.firstOrNull()?.valorTexto ?: variante.sku
                    Text(text = variantName)
                },
                leadingIcon = {
                    if (variante.id == selectedId) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Seleccionado", modifier = Modifier.size(FilterChipDefaults.IconSize))
                    }
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = Color.White
                )
            )
        }
    }
}


// ----------------------------------------------------------------------
// 3. EL DIBUJANTE (ProductDetailContent)
// ----------------------------------------------------------------------

@Composable
fun ProductDetailContent(
    details: ProductDetailsDto,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var quantity by remember { mutableIntStateOf(1) }
    val scrollState = rememberScrollState()

    // Lógica para determinar la variante activa
    var selectedVariantId by remember { mutableLongStateOf(details.variantes.firstOrNull()?.id ?: -1L) }
    val activeVariant = details.variantes.find { it.id == selectedVariantId }
    val currentPrice = activeVariant?.precio ?: details.precioBase
    val priceFormatted = "S/${String.format("%.2f", currentPrice)}"
    val activeImages = activeVariant?.imagenes ?: details.imagenes // Imágenes de la variante o del producto

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 80.dp)
        ) {
            Spacer(modifier = Modifier.height(18.dp))
            // TOP BAR MANUAL (Se mantiene así para no interferir con Scaffold)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", modifier = Modifier.size(44.dp), tint = MaterialTheme.colorScheme.onBackground)
                }
                Text(
                    text = details.nombre,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Divider(thickness = 1.2.dp, color = MaterialTheme.colorScheme.onBackground.copy(0.10f))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                ProductImageCarousel(activeImages) // ⬅️ IMAGEN DINÁMICA

                Spacer(modifier = Modifier.height(24.dp))

                Text(text = details.nombre, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onBackground)
                Spacer(modifier = Modifier.height(8.dp))

                Row( /* ... Precio y Share Button ... */ ) {
                    Text(text = priceFormatted, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    // ... (Share Button) ...
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // --- SELECTOR DE VARIANTE ---
            if (details.variantes.isNotEmpty()) {
                ProductSection(title = "Opciones", content = {
                    ProductVariantSelector(
                        variantes = details.variantes,
                        selectedId = selectedVariantId,
                        onSelect = { variantId -> selectedVariantId = variantId }
                    )
                })
            }

            // --- FICHA TÉCNICA DINÁMICA ---
            ProductSection(title = "Ficha Técnica", content = {
                ProductAttributesList(activeVariant?.atributos ?: details.atributos)
            })

            // ... (Resto de secciones) ...
            ProductSection(title = "Descripción", content = { Text(text = details.descripcion ?: "Sin descripción.", fontSize = 14.sp, color = Color(0xFF666666)) })
            ProductSection(title = "Productos Relacionados", content = { Text("Lista de productos relacionados...") })

            Spacer(modifier = Modifier.weight(1f).height(16.dp))
        }

        // --- ROW INFERIOR (Cantidad y Carrito) ---
        // [Tu Row inferior de cantidad y carrito se mantiene igual]
    }
}


// ----------------------------------------------------------------------
// 4. EL CONECTOR ESTATAL (Entry Point)
// ----------------------------------------------------------------------

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsProductoScreen(
    productId: Int,
    onBack: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Solo envolvemos el Content en el Box de estado
    Box(modifier = Modifier.fillMaxSize()) {
        when (val currentState = state) {
            is UiState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
            is UiState.Error -> {
                Text(text = "Error: ${currentState.message}", color = Color.Red, modifier = Modifier.align(Alignment.Center))
            }
            is UiState.Success -> {
                // Pasamos el DTO de detalle al componente de dibujo
                ProductDetailContent(
                    details = currentState.data,
                    onBack = onBack
                )
            }
            else -> {}
        }
    }
}