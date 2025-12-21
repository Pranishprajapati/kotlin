package com.example.pranish

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.pranish.ui.theme.Blue
import com.example.pranish.ui.theme.white

class ProfileDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProfileDashboardBody()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileDashboardBody() {

    val context = LocalContext.current
    val activity = context as Activity


    data class NavItem(val label: String, val icon: Int)
    var selectedIndex by remember { mutableIntStateOf(0) }

    val navList = listOf(
        NavItem(label = "Home", icon = R.drawable.home),
        NavItem(label = "Search", icon = R.drawable.search),
        NavItem(label = "Add", icon = R.drawable.add),
        NavItem(label = "Video", icon = R.drawable.video),
        NavItem(label = "Profile", icon = R.drawable.person)
    )

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val intent = Intent(context, AddProductActivity::class.java)
                context.startActivity(intent)
            }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Product")
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Ecommerce") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Blue,
                    navigationIconContentColor = white,
                    titleContentColor = white,
                    actionIconContentColor = white
                ),
                navigationIcon = {
                    IconButton(onClick = { activity.finish() }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_arrow_back_24),
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            painter = painterResource(R.drawable.search),
                            contentDescription = "Search"
                        )
                    }
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(
                            painter = painterResource(R.drawable.person),
                            contentDescription = "Profile"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                navList.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(item.icon),
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index }
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()

                .padding(padding)


        ) {

            // TODO: Show different screens based on selectedIndex

           when(selectedIndex){
                0 -> HomeScreen()
//                1 -> SearchScreen()
//                2 -> AddScreen()
//                3 -> VideoScreen()
//                4 -> ProfileScreen()
            }

        }
    }
}
