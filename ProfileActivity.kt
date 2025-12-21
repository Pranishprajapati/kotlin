package com.example.pranish

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pranish.ui.theme.PranishTheme


class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PranishTheme{
                ProfileScreen()
            }
        }
    }
}

@Composable
@Preview(showBackground = true)
fun ProfileScreen() {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // -------------------- TOP BAR --------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_arrow_back_24),
                    contentDescription = "Back",
                    modifier = Modifier.size(30.dp)
                )

                Text(
                    text = "                        pranish",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    painter = painterResource(R.drawable.baseline_more_vert_24),
                    contentDescription = "More",
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))


            // ---------------- PROFILE IMAGE + COUNTS ----------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Profile Image
                Image(
                    painter = painterResource(R.drawable.pranish),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(90.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("13", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Posts", fontSize = 14.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("1223", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Followers", fontSize = 14.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("618", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text("Following", fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))


            // ---------------- BIO SECTION ----------------
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {

                Text(
                    text = "Pranish Prajapati",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )


                // Hashtag + Link (Clickable style)

                Spacer(modifier = Modifier.height(5.dp))
            }

            Spacer(modifier = Modifier.height(15.dp))


            // ---------------- ACTION BUTTONS ----------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Button(
                    onClick = {},
                    modifier = Modifier.weight(1f).height(38.dp)
                ) {
                    Text("Follow")
                }

                Spacer(modifier = Modifier.width(6.dp))

                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.weight(1.2f).height(38.dp)
                ) {
                    Text("Message")
                }

                Spacer(modifier = Modifier.width(6.dp))

                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.weight(1f).height(38.dp)
                ) {
                    Text("Email")
                }

                Spacer(modifier = Modifier.width(6.dp))

                OutlinedButton(
                    onClick = {},
                    modifier = Modifier.size(38.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_arrow_drop_down_24),
                        contentDescription = "Dropdown"
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))


            // ---------------- STORY HIGHLIGHTS ----------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                StoryItem(R.drawable.lion, "Story 1")
                StoryItem(R.drawable.naruto, "Story 2")
                StoryItem(R.drawable.feather, "Story 3")
                StoryItem(R.drawable.eye, "Story 4")
                StoryItem(R.drawable.earth, "Story 5")
            }
        }
    }
}

// ---------------- STORY ITEM COMPOSABLE ----------------
@Composable
fun StoryItem(image: Int, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(image),
            contentDescription = label,
            modifier = Modifier
                .size(70.dp)
                .clip(CircleShape)
                .border(2.dp, Color.Gray, CircleShape),
            contentScale = ContentScale.Crop
        )
        Text(
            text = label,
            fontSize = 12.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}