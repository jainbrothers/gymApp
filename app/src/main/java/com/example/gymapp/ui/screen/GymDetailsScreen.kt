package com.example.gymapp.ui.screen

import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gymapp.R
import com.example.gymapp.model.Address
import com.example.gymapp.model.Gym
import com.example.gymapp.model.Location
import com.example.gymapp.model.Timings
import com.example.gymapp.ui.screen.viewmodel.GymDetailsViewModel
import com.example.gymapp.ui.theme.MyApplicationTheme
import com.example.gymapp.ui.utils.AutoSlidingCarousel
import com.google.accompanist.pager.ExperimentalPagerApi
import java.util.Locale

@Composable
fun ShowGymTitle(
    title: String
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title.uppercase(Locale.ROOT),
            fontWeight = FontWeight.Bold,
            style = TextStyle(fontSize = 25.sp),
            modifier = Modifier.padding(20.dp)
        )
    }
}

@Composable
fun ShowGymAddress(gymName:String, address: Address) {
    val context = LocalContext.current
    Column() {
        Row() {
            Icon(
                painter = painterResource(id = R.drawable.round_location_on_24),
                contentDescription = null, // decorative element
                modifier = Modifier
                    .padding(5.dp)
                    .size(30.dp)
            )
            Column(
                Modifier
                    .padding(10.dp)
                    .weight(1F)
            ) {
                Text(text = address.locality, fontWeight = FontWeight.Bold)
                Text(text = address.streetNameAndNumber + " " + address.locality)
            }
            IconButton(onClick = {
                val gmmIntentUri = Uri.parse(
                    "geo:${address.location.latitude},${address.location.longitude}?q=$gymName")

                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
                // Make the Intent explicit by setting the Google Maps package
                    mapIntent.setPackage("com.google.android.apps.maps")
                // Attempt to start an activity that can handle the Intent
                mapIntent.resolveActivity(context.packageManager)?.let {
                    Log.d("GymDetailsScreen", "got the package manager")
                    context.startActivity(mapIntent)
                }
                context.startActivity(mapIntent)
                Log.d("GymDetailsScreen", "clicked on Icon Button")
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.round_near_me_24),
                    contentDescription = null,
                    modifier = Modifier.padding(5.dp),
                )
            }
        }
    }
}


data class TimingsList(val title: String, val timingsList: List<Timings>)


@Composable
fun ShowTimings(timingsList: List<Timings>) {
    // Sample data
    var expanded by remember { mutableStateOf(false) }
    val items = listOf(
        TimingsList("Gym Timings", timingsList),
    )
    LazyColumn {
        items.forEach { group ->
            item {
                Text(
                    text = group.title,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .padding(vertical = 16.dp, horizontal = 24.dp)
                        .clickable { expanded = !expanded }
                )
            }

            if (expanded) {
                group.timingsList.forEach { schedule ->
                    item {
                        Row() {
                            Text(
                                text = schedule.day,
                                modifier = Modifier
                                    .padding(horizontal = 48.dp, vertical = 8.dp)
                            )
                            Text(
                                text = schedule.beginHour.toString() +":"+schedule.beginMinute + " - "
                                        + schedule.endHour.toString() +":"+schedule.endMinute,
                                modifier = Modifier.padding(horizontal = 48.dp, vertical = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShowAmenity(facility: String) {
    val facilityResId: Int = when (facility) {
        "GYM" -> R.drawable.gym
        "YOGA" -> R.drawable.yoga
        "Parking" -> R.drawable.round_directions_car_24
        "Locker" -> R.drawable.round_lock_24
        "CCTV" -> R.drawable.round_camera_outdoor_24
        else -> {
            R.drawable.gym
        }
    }
    Column(modifier = Modifier.padding(start = 20.dp)) {
        Icon(
            painter = painterResource(id = facilityResId),
            contentDescription = null, // decorative element
            modifier = Modifier.size(30.dp)
        )
        Text(text = facility)
    }
}



@Composable
fun ShowAmenities(facilities: List<String>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Amenities",
            Modifier.padding(top = 10.dp, bottom = 10.dp, start = 10.dp),
            fontWeight = FontWeight.Bold
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(80.dp),

            // content padding
            contentPadding = PaddingValues(
                start = 2.dp,
                top = 6.dp,
                end = 2.dp,
                bottom = 6.dp
            ),
            content = {
                items(facilities) { facility ->
                    ShowAmenity(facility)
                }
            }
        )
    }
}

@Composable
fun ShowWorkouts(workouts: List<String>) {
    Column() {
        Text(
            text = "Available Workouts",
            Modifier.padding(top = 10.dp, bottom = 10.dp, start = 10.dp),
            fontWeight = FontWeight.Bold
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(80.dp),
            // content padding
            contentPadding = PaddingValues(
                start = 2.dp,
                top = 6.dp,
                end = 2.dp,
                bottom = 6.dp
            ),
            content = {
                items(workouts) { workout ->
                    ShowAmenity(workout)
                }
            }
        )
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ShowGymDetailsPage(
    gymDetailsViewModel: GymDetailsViewModel = hiltViewModel()
) {
    val uiState by gymDetailsViewModel.gymDetailsUiState.collectAsState()
    val imageUrls = listOf(uiState.gym.imageUrls, uiState.gym.imageUrls)
    Column(Modifier.fillMaxSize()) {
        AutoSlidingCarousel(
            itemsCount = imageUrls.size,
            itemContent = { index ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imageUrls[index])
                        .build(),
                    contentDescription = "Gym Images",
                    contentScale = ContentScale.Crop, // need to check this
                    modifier = Modifier.height(200.dp) // to put it as constant
                )
            },
            autoScroll = false
        )
        ShowGymTitle(uiState.gym.name)
        Divider(thickness = 1.dp)
        ShowGymAddress(uiState.gym.name, uiState.gym.address)
        Divider(thickness = 1.dp)
        ShowWorkouts(uiState.gym.activities)
        Divider(thickness = 1.dp)
        ShowTimings(uiState.gym.timings)
        Divider(thickness = 1.dp)
        ShowAmenities(uiState.gym.amenities)
        Divider(thickness = 1.dp)
//        TabbedPages()
    }
}


@Composable
fun BookWorkoutSession(){
    Column(Modifier.fillMaxWidth().height(80.dp)) {
        Button(
            onClick = {},
            Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Book Session")
        }
    }
}

@Composable
fun ShowWorkoutOptions(
    selectedTabIndex: Int,
    tabTitles: List<String>,
    onSelectedTab: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedTabIndex,
        modifier = Modifier.fillMaxWidth()
    ) {
        tabTitles.forEachIndexed { index, title ->
            Tab(
                text = { Text(title) },
                selected = selectedTabIndex == index,
                onClick = { onSelectedTab(index) }
            )
        }
    }
}


@Composable
fun TabbedPages() {
    val tabs = listOf("Gym Sessions")
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column {
        TabRow(selectedTabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTabIndex == index,
                    onClick = { selectedTabIndex = index },
                    text = { Text(title) }
                )
            }
        }

        when (selectedTabIndex) {
            0 -> {
                // Content for Tab 1
                Text("Content for Tab 1")
            }
            1 -> {
                // Content for Tab 2
                Text("Content for Tab 2")
            }
            2 -> {
                // Content for Tab 3
                Text("Content for Tab 3")
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GymDetailsScreenPreview() {
    MyApplicationTheme {
        val gym: Gym = Gym()
        Column() {
            ShowWorkouts(gym.activities)
            Divider(thickness = 1.dp)
//            BookWorkoutSession()
//            Divider(thickness = 1.dp)
//            ShowTimings(gym.timings)
//            Divider(thickness = 1.dp)
//            ShowAmenities(gym.amenities)
//            Divider(thickness = 1.dp)
//            ShowWorkoutOptions(
//                selectedTabIndex = 0,
//                tabTitles = listOf("Gym", "Yoga", "Dance"),
//                onSelectedTab = {}
//            )
        }
    }
}

