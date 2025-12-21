package com.example.pranish

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pranish.ui.theme.PranishTheme

// ----------------------- DATA MODELS -----------------------
data class Story(val imageRes: Int, val title: String)

val recentlyPlayed = listOf(
    Story(R.drawable.lana, "Lana Del Rey"),
    Story(R.drawable.eminen, "Eminem")
)

val wrappedStories = listOf(
    Story(R.drawable.top, "Your Top Songs 2025"),
    Story(R.drawable.artists, "Your Artists Revealed")
)

val editorsPicks = listOf(
    Story(R.drawable.ed, "Ed Sheeran, Big Sean, Post Malone"),
    Story(R.drawable.mitski, "Mitski, Tame Impala, Charli XCX")
)

// ----------------------- ACTIVITY -----------------------
class SpotifyHomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PranishTheme {
                SpotifyHomeScreen()
            }
        }
    }
}

// ----------------------- COMPOSABLES -----------------------
@Composable
fun SpotifyHomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF121212), Color(0xFF1E1E1E))
                )
            )
            .padding(16.dp)
    ) {
        TopBar()
        Spacer(modifier = Modifier.height(20.dp))

        Section("Recently played", recentlyPlayed)
        Spacer(modifier = Modifier.height(25.dp))

        Section("Your 2025 in review", wrappedStories)
        Spacer(modifier = Modifier.height(25.dp))

        Section("Editor's picks", editorsPicks)
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Recently played",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.weight(2f)
        )

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icons = listOf(
                R.drawable.lana to 26.dp,
                R.drawable.eminen to 26.dp,
                R.drawable.ed to 26.dp
            )

            icons.forEachIndexed { index, (icon, size) ->
                Image(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier = Modifier
                        .size(size)
                        .clip(if (index == 0) CircleShape else RoundedCornerShape(10.dp))
                )
                if (index != icons.lastIndex) Spacer(modifier = Modifier.width(15.dp))
            }
        }
    }
}

@Composable
fun Section(title: String, stories: List<Story>) {
    Text(
        text = title,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
        color = Color.White
    )
    Spacer(modifier = Modifier.height(20.dp))
    LazyRow(horizontalArrangement = Arrangement.spacedBy(30.dp)) {
        items(stories) { story ->
            StoryCard(story)
        }
    }
}

@Composable
fun StoryCard(story: Story) {
    Card(
        modifier = Modifier
            .width(170.dp)
            .height(180.dp),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Black)
    ) {
        Column {
            Image(
                painter = painterResource(id = story.imageRes),
                contentDescription = story.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black.copy(alpha = 0.7f))
                    .padding(1.dp)
            ) {
                Text(text = story.title, fontSize = 10.sp, color = Color.White)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSpotify() {
    PranishTheme {
        SpotifyHomeScreen()
    }
}
