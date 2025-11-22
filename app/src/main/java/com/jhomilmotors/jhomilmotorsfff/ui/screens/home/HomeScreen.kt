package com.jhomilmotors.jhomilmotorsfff.ui.screens.home

import android.R.attr.contentDescription
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.jhomilmotors.jhomilmotorsfff.R
import com.jhomilmotors.jhomilmotorsfff.data.model.UiState
import com.jhomilmotors.jhomilmotorsfff.navigation.AppScreens
import com.jhomilmotors.jhomilmotorsfff.ui.theme.JhomilMotorsShopTheme
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.SessionViewModel
import com.jhomilmotors.jhomilmotorsfff.ui.viewmodels.home.CategoryViewModel

data class contentHeaderCarrusel(
    val id: Int,
    val title: String,
    val description: String,
    val imageResId: Int,
    val buttonText: String,
)

data class Product(
    val id: Int,
    val imageUrl: Int,
    val title: String,
    val price: Double,
    val originalPrice: Double? = null,
    val soldCount: String,
)

//Micro composables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar() {
    var text by remember { mutableStateOf("") }

    OutlinedTextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(
            text= "Buscar",
            color = MaterialTheme.colorScheme.onBackground.copy(0.50f)
        ) },
        shape = MaterialTheme.shapes.extraLarge,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = MaterialTheme.colorScheme.onBackground.copy(0.23f),
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onBackground.copy(0.23f),
            disabledIndicatorColor = Color.Gray,
            focusedContainerColor = Color(0xFFF6F9FF).copy(0.45f),
            unfocusedContainerColor = Color(0xFFF6F9FF).copy(alpha = 0.45f),
        ),
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.search),
                contentDescription = "Icono de lupa"
            )
        },
        trailingIcon = {
            Row (

                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.padding(end = 12.dp)
            ) {
                IconButton(
                    onClick = { /* Acción al hacer clic */ },
                    modifier = Modifier
                        .size(34.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = CircleShape
                        ),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.filter_icon),
                        contentDescription = "Icono de filtro"
                    )
                }
                IconButton(
                    onClick = { /* Acción al hacer clic */ },
                    modifier = Modifier
                        .size(34.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onBackground,
                            shape = CircleShape
                        ),
                    colors = IconButtonDefaults.iconButtonColors(
                        contentColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.filter_icon),
                        contentDescription = "Icono de filtro"
                    )
                }

            }
        },

        )
}

@Composable
fun CarruselHeaderItem(
    data: contentHeaderCarrusel,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(0.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary.copy(0.12f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 20.dp, top = 12.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = data.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(0.82f),

                    )
                Text(
                    text = data.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Button(

                    onClick = onButtonClick,
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary
                    ),
                ) {
                    Text(

                        text = data.buttonText,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            Image(
                painter = painterResource(id = data.imageResId),
                contentDescription = data.description,
                modifier = Modifier
                    .width(147.dp)
                    .height(147.dp)
                    .clipToBounds(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun ProductItem(product: Product) {
    Card(
        modifier = Modifier
            .width(154.dp) // Ancho fijo para cada tarjeta
            .padding(8.dp),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.tertiary.copy(0.15f))
                    .clip(MaterialTheme.shapes.large),
                contentAlignment = Alignment.TopEnd
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(142.dp)
                ){
                    Image(
                        painter = painterResource(id = product.imageUrl),
                        contentDescription = product.title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                IconButton(
                    onClick = { /* Acción para agregar al carrito */ },
                    modifier = Modifier

                        .padding(8.dp).shadow(0.8.dp, CircleShape)
                        .background(color = MaterialTheme.colorScheme.background, shape = CircleShape)
                        .size(32.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.accesorios),
                        contentDescription = "Agregar al carrito",
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    text = product.title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "S/${product.price}",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.secondary.copy(0.8f) // Color para el precio de oferta
                )

                // solo muestra el precio original si no es nulo
                if (product.originalPrice != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "S/${product.originalPrice}",
                        style = TextStyle(
                            fontSize = 12.sp,
                            textDecoration = TextDecoration.LineThrough,
                            color = MaterialTheme.colorScheme.onBackground.copy(0.5f)
                        )
                    )
                }
            }

            Text(
                text = product.soldCount,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Normal) ,
                color = MaterialTheme.colorScheme.primary,

                )
        }
    }
}
@Composable
fun CategoryItemView(
    item: CategoryItem
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(74.dp)
                .background(MaterialTheme.colorScheme.tertiary.copy(0.12f), CircleShape).clickable { item.onClick() },
            contentAlignment = Alignment.Center

        ) {
            AsyncImage(
                model = item.iconUri,
                contentDescription = "Icono de ${item.nombre}",
                modifier = Modifier.size(34.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = item.nombre,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}
@Composable
fun ProductsCarousel(products: List<Product>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            contentPadding = PaddingValues(horizontal = 12.dp)
        ) {
            items(products) { product ->
                ProductItem(product = product)
            }
        }
    }
}
data class CategoryItem (
    val id: Int,
    val nombre : String,
    val iconUri : String?,
    val onClick : () -> Unit,
)

@Composable
fun Principal(
    navController: NavController,
    sessionViewModel: SessionViewModel = hiltViewModel(),
    categoryViewModel: CategoryViewModel  = hiltViewModel()
){

    // ===== Estados de sesión =====
    val sessionValid by sessionViewModel.sessionValid.collectAsState()
    val isChecking by sessionViewModel.isChecking.collectAsState()

    // ===== Estados de Category =====
    val category by categoryViewModel.categories.collectAsState()

    // ===== Validar sesión al entrar =====
    LaunchedEffect(Unit) {
        sessionViewModel.checkSession()
    }

    LaunchedEffect(sessionValid, isChecking) {
        if (!isChecking && !sessionValid) {
            navController.navigate(AppScreens.Login.route) {
                popUpTo(0) { inclusive = true }
            }
        }
    }

    // ===== Cargar Categories =====
    LaunchedEffect(Unit ) {
        categoryViewModel.loadCategories()
    }

    val scrollState = rememberScrollState()
    // La lista se declara aquí para que sea accesible para todo el Composable.
    val items = listOf(
        contentHeaderCarrusel(1, "Productos recién llegados", "35 % de dsct...", R.drawable.biker_header, "Compra ahora"),
        contentHeaderCarrusel(2, "Otro Título", "Otra descripción", R.drawable.biker_header, "Ver más")
    )
    val offerProducts = listOf(
        Product(1, R.drawable.motul_oil, "7100 Aceite motor ...", 46.00, 53.00, "50 + vendidos"),
        Product(2, R.drawable.motul_oil, "7100 Aceite motor ...", 46.00, 53.00, "50 + vendidos"),
        Product(3, R.drawable.motul_oil, "7100 Aceite motor ...", 46.00, 53.00, "50 + vendidos"),
    )
    val mostPurchasedProducts = listOf(
        Product(1, R.drawable.motul_oil, "7100 Aceite motor ...", 26.00, originalPrice = null, "40 + vendidos"),
        Product(2, R.drawable.motul_oil, "7100 Aceite motor ...", 67.00, originalPrice = null, "20 + vendidos"),
        Product(3, R.drawable.motul_oil, "7100 Aceite motor ...", 90.00, originalPrice = null, "70 + vendidos"),
    )

    when {
        isChecking -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        !sessionValid -> {
            // Sesión inválida (se redirige automáticamente)
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        else ->{
            Column(
                modifier = Modifier.fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                var currentPage by remember { mutableStateOf(0) }
                Column(modifier = Modifier.padding(horizontal = 19.dp)) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            painter = painterResource(R.drawable.logo_jhomil),
                            contentDescription = "Logo de la empresa Jhomil Motors"
                        )
                        Icon(
                            painter = painterResource(id= R.drawable.notification_icon),
                            contentDescription = "Icono de notificaciones"
                        )
                    }
                    SearchBar()
                    Spacer(modifier = Modifier.height(10.dp))
                    LazyRow (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(19.dp)

                    ){
                        // El LazyRow ahora usa la lista que declaraste arriba.
                        items(items) { item ->
                            CarruselHeaderItem(
                                data = item,
                                onButtonClick = {},
                                modifier = Modifier.fillParentMaxWidth()
                            )
                        }
                    }
                    // Aquí puedes ver los indicadores de paginación
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Aquí usamos la lista 'items' que ahora es accesible
                        items.forEachIndexed { index, _ ->
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        color = if (index == currentPage) MaterialTheme.colorScheme.onBackground else Color.Gray.copy(
                                            0.7f
                                        ),
                                        shape = CircleShape
                                    )
                                    .padding(horizontal = 4.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(25.dp))
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(9.dp)
                    .background(MaterialTheme.colorScheme.secondary.copy(0.05f)))

                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 19.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        text = "Categorías",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                    )
                    Text(
                        text = "Ver todo",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }


                when (val state = category){
                    is UiState.Loading -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            repeat(4) {
                                Box(
                                    modifier = Modifier
                                        .size(74.dp)
                                        .background(MaterialTheme.colorScheme.tertiary.copy(0.12f), CircleShape)
                                )
                            }
                        }
                    }
                    is UiState.Error -> {
                        Text("Error al cargar categorías, revisa tu conexión a internet.", color = Color.Red)
                    }
                    is UiState.Success -> {
                        LazyRow (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(19.dp)
                        ){
                            items(state.data) {categoriaApi ->
                                CategoryItemView(
                                    item = CategoryItem(
                                        id = categoriaApi.id.toInt(),
                                        nombre = categoriaApi.nombre,
                                        iconUri = categoriaApi.urlImagenCompleta,
                                        onClick = {navController.navigate(AppScreens.ProductList.createRoute(categoriaApi.id.toInt()))}
                                    )
                                )
                            }
                        }
                    }
                    else -> {}
                }


                Spacer(modifier = Modifier.height(25.dp))
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(9.dp)
                    .background(MaterialTheme.colorScheme.secondary.copy(0.05f)))

                Text(
                    modifier = Modifier.padding(horizontal = 19.dp, vertical = 12.dp),
                    text = "En oferta!  ",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                ProductsCarousel(offerProducts)
                Spacer(modifier = Modifier.height(25.dp))
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(9.dp)
                    .background(MaterialTheme.colorScheme.secondary.copy(0.05f)))

                Text(
                    modifier = Modifier.padding(horizontal = 19.dp, vertical = 12.dp),
                    text = "Más comprados",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                ProductsCarousel(mostPurchasedProducts)
            }
        }
    }
}
