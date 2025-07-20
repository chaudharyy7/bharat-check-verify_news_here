package com.example.bharatcheck.scaffold

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.bharatcheck.presentations.HomeScreen
import com.example.bharatcheck.presentations.newsScreen
import com.example.bharatcheck.presentations.profileScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldUi(name: String, navController: NavController) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF4E4EE7),
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Home",tint = Color.White) },
                    label = { Text("Home",color = Color.White) },
                    selected = selectedTab == 0,
                    onClick = {
                        selectedTab = 0
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color(0xFF0F52BA) // background behind selected item
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Newspaper, contentDescription = "News",tint = Color.White) },
                    label = { Text("News",color = Color.White) },
                    selected = selectedTab == 1,
                    onClick = {
                        selectedTab = 1
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color(0xFF0F52BA)
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Person, contentDescription = "Profile",tint = Color.White) },
                    label = { Text("Profile",color = Color.White) },
                    selected = selectedTab == 2,
                    onClick = {
                        selectedTab = 2
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        unselectedIconColor = Color.Gray,
                        selectedTextColor = Color.White,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color(0xFF0F52BA) // background behind selected item
                    )
                )
            }
        }
    ) { innerPadding ->
        when (selectedTab) {
            0 -> HomeScreen(
                name = name,
                navController = navController,
                modifier = Modifier.padding(innerPadding)
            )
            1 -> newsScreen(
                navController = navController,
                modifier = Modifier.padding(innerPadding))
            2 -> profileScreen(
                navController = navController,
                modifier = Modifier.padding(innerPadding))
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ScaffoldPreview() {
//    val navController = rememberNavController()
//    ScaffoldUi(navController = navController)
//}
