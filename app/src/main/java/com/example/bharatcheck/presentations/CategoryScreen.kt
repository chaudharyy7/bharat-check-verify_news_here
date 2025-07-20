package com.example.bharatcheck.presentations

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.bharatcheck.NewsScreenViewModel

@Composable
fun categoriesScreen(
    categories: String,
    navController: NavController,
    viewModel: NewsScreenViewModel = remember { NewsScreenViewModel() }
) {
    LaunchedEffect(categories) {
        viewModel.getNews(categories.lowercase())
    }

    val articles by viewModel.articles.observeAsState(emptyList())

    Column(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF0F52BA),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 0.dp,
                bottomEnd = 15.dp,
                bottomStart = 15.dp
            ),
        ) {
            Text(
                text = categories,
                fontSize = 25.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 55.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(articles) { article ->
                ArticleCard(article = article,navController = navController)
            }
        }
    }
}
