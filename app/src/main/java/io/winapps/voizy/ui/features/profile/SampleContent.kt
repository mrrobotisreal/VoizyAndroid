package io.winapps.voizy.ui.features.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PhotosContent() {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("Photos go here...")
    }
}

@Composable
fun AboutContent() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Email: [user's email]")
        Text("Preferred Name: ...")
        Text("First Name: ...")
        Text("Last Name: ...")
        Text("Birth Date: ...")
        Text("City of Residence: ...")
        Text("Place of Work: ...")
        // etc...
    }
}

@Composable
fun FriendsContent() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Friends list or suggestions go here...")
    }
}
