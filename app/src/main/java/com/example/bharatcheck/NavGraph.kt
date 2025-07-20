package com.example.bharatcheck

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bharatcheck.presentations.LoginUI
import com.example.bharatcheck.presentations.SignUp
import com.example.bharatcheck.presentations.categoriesScreen
import com.example.bharatcheck.presentations.imageVerificationScreen
import com.example.bharatcheck.presentations.linkVerificationScreen
import com.example.bharatcheck.presentations.newsArticleScreen
import com.example.bharatcheck.presentations.newsScreen
import com.example.bharatcheck.presentations.profileScreen
import com.example.bharatcheck.presentations.textVerificationScreen
import com.example.bharatcheck.scaffold.ScaffoldUi

@Composable
fun MainApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("home") {

        }
        composable("sign_up") {
            SignUp(navController)
        }
        composable("log_in") {
            LoginUI(navController)
        }

        composable("link_screen") {
            linkVerificationScreen()
        }
        composable("image_screen") {
            imageVerificationScreen()
        }
        composable("video_screen") {

        }
        composable("text_screen") {
            textVerificationScreen()
        }
        composable("news_screen") {
            newsScreen(navController)
        }
        composable("profile_screen") {
            profileScreen(navController)
        }
        composable("scaffold_screen/{FirstName}") {backStackEntry ->
            val firstName = backStackEntry.arguments?.getString("FirstName") ?: "Guest"
            ScaffoldUi (firstName,navController = navController)
        }
        composable("category/{categoryName}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("categoryName") ?: "Everything"
            categoriesScreen(categories = category, navController = navController)
        }
        composable("webview_screen/{ArticleUrl}") { backStackEntry ->
            val article = backStackEntry.arguments?.getString("ArticleUrl") ?: "https://www.google.com"
            newsArticleScreen(article)
        }
    }
}
