package com.example.bharatcheck.presentations

import android.R.attr.fontWeight
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun profileScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val auth = remember { FirebaseAuth.getInstance() }
    val database = FirebaseDatabase.getInstance().getReference("Users")
    val userId = auth.currentUser?.uid

    var fullName by remember { mutableStateOf("Loading...") }
    var email by remember { mutableStateOf("Loading...") }
    var phoneNumber by remember { mutableStateOf("Loading...") }

    LaunchedEffect(userId) {
        userId?.let { uid ->
            database.child(uid).get().addOnSuccessListener { snapshot ->
                val first = snapshot.child("firstName").value?.toString() ?: ""
                val last = snapshot.child("lastName").value?.toString() ?: ""
                fullName = "$first $last"
                email = snapshot.child("email").value?.toString() ?: "N/A"
                phoneNumber = snapshot.child("phoneNumber").value?.toString() ?: "N/A"
            }.addOnFailureListener {
                fullName = "Failed to load"
                email = "Error"
                phoneNumber = "Error"
            }
        } ?: run {
            fullName = "Guest"
            email = "N/A"
            phoneNumber = "N/A"
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F52BA)),
            shape = RoundedCornerShape(0.dp, 0.dp, 15.dp, 15.dp)
        ) {
            Text(
                text = "Profile",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 20.dp, top = 55.dp),
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Single Card for All Details
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF0F52BA),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                ProfileItem(label = "Full Name", value = fullName)
                ProfileItem(label = "Email", value = email)
                ProfileItem(label = "Phone Number", value = phoneNumber)

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Sign Out",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable {
                            auth.signOut()
                            navController.navigate("log_in")
                        }
                        .padding(vertical = 8.dp),
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
fun ProfileItem(label: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(
            text = "$label:",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        Text(
            text = value,
            fontSize = 18.sp,
            color = Color.White
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun profileScreenPreview() {
    profileScreen(navController = rememberNavController())
}
