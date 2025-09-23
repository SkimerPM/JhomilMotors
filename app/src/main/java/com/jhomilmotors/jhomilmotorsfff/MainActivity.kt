package com.jhomilmotors.jhomilmotorsfff

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.IconButton
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import com.jhomilmotors.jhomilmotorsfff.ui.theme.JhomilMotorsShopTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JhomilMotorsShopTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                    ) {
                        Principal()
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Poppins $name!",
        modifier = modifier
    )
}

//micro composables:
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
                        .background(color = MaterialTheme.colorScheme.onBackground, shape = CircleShape),
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
                        .background(color = MaterialTheme.colorScheme.onBackground, shape = CircleShape),
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


data class contentHeaderCarrusel(
    val id: Int,
    val title: String,
    val description: String,
    val imageResId: Int,
    val buttonText: String,
)

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
                .fillMaxWidth().height(130.dp),

            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f).padding(start= 20.dp, top = 12.dp, bottom = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = data.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground,

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
                modifier = Modifier.width(147.dp).height(147.dp).clipToBounds(),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun Principal(){
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.padding(horizontal = 19.dp)) {
            Row (
                modifier = Modifier
                    .fillMaxWidth().
                    height(56.dp),

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
                modifier = Modifier.fillMaxWidth()
            ){
                items(listOf(
                    contentHeaderCarrusel(1, "Productos recién llegados", "35 % de dsct...", R.drawable.biker_header, "Compra ahora"),
                    contentHeaderCarrusel(2, "Otro Título", "Otra descripción", R.drawable.biker_header, "Ver más")
                )) { item ->
                    CarruselHeaderItem(
                        data = item,
                        onButtonClick = {},
                        modifier = Modifier.fillParentMaxWidth()
                    )
                }
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PrincipalPreview() {
    JhomilMotorsShopTheme {

        Principal()

    }
}