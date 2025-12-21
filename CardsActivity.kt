package com.example.pranish

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pranish.ui.theme.Blue
import com.example.pranish.ui.theme.PranishTheme
import com.example.pranish.ui.theme.green
import com.example.pranish.ui.theme.light

class CardsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PranishTheme {
                MainScreen()
            }
        }
    }
}


data class CategoryItem(
    val title: String,
    val contentCount: Int,
    val icon: ImageVector? = null,
    val iconPainter: Int? = null,
)

val mockCategoryItem = listOf(
    CategoryItem("Text", 15, iconPainter = R.drawable.text),
    CategoryItem("Address", 9, iconPainter = R.drawable.address),
    CategoryItem("Character", 1, iconPainter = R.drawable.character),
    CategoryItem("Bank card", 5, iconPainter = R.drawable.bankcard),
    CategoryItem("Password", 21, iconPainter = R.drawable.password),
    CategoryItem("Logistics", 10, iconPainter = R.drawable.logistics),
)


@Composable
fun MainScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(green)
    ) {

        // TOP HEADER
        TopHeaderBars()

        // MAIN WHITE AREA
        Column(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp))
                .background(Color.White)
                .padding(25.dp)
        ) {

            // GRID OF CARDS
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(mockCategoryItem.size) { index ->
                    CategoryItemCard(mockCategoryItem[index])
                }
            }

            SettingButton()
        }
    }
}


@Composable
fun TopHeaderBars() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 45.dp, start = 24.dp, end = 24.dp, bottom = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Card",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Simple and easy to use app",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
        }

        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White)
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Profile",
                tint = light,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Composable
fun CategoryItemCard(item: CategoryItem) {
    Card(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .aspectRatio(1f)
            .clickable { }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // ICON
            if (item.iconPainter != null) {
                Image(
                    painter = painterResource(id = item.iconPainter),
                    contentDescription = item.title,
                    modifier = Modifier.size(60.dp)
                )
            } else if (item.icon != null) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = light,
                    modifier = Modifier.size(60.dp)
                )
            } else {
                Spacer(modifier = Modifier.size(60.dp))
            }

            // TITLE & COUNT
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = item.title,
                    color = Blue,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${item.contentCount} items content",
                    color = Blue,
                    fontSize = 12.sp
                )
            }
        }
    }
}

@Composable
fun SettingButton() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF0F4F7))
            .clickable { }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Settings",
            tint = Blue,
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = "Settings",
                color = Blue,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = "Fingerprint mode and backup",
                color = Blue,
                fontSize = 12.sp
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewCardHome() {
    PranishTheme {
        MainScreen()
    }
}
