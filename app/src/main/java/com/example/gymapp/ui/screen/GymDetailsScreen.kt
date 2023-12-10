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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
                                text = schedule.openingHour.toString() +":"+schedule.openingMinute + " - "
                                        + schedule.closingHour.toString() +":"+schedule.closingMinute,
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
fun ShowActivity(gymId: String,
                 activity: String,
                 onClickBookSession: (String, String) -> Unit) {
    val activityResId: Int = when (activity) {
        "GYM" -> R.drawable.gym
        "YOGA" -> R.drawable.yoga
        else -> {
            R.drawable.gym
        }
    }
    Column(modifier = Modifier
        .padding(start = 20.dp)
        .clickable(onClick = {
            onClickBookSession(gymId, activity)
        })
    ) {
        Icon(
            painter = painterResource(id = activityResId),
            contentDescription = null, // decorative element
            modifier = Modifier.size(30.dp)
        )
        Text(text = activity)
    }
}


@Composable
fun ShowAmenity(amenity: String) {
    val amenityResId: Int = when (amenity) {
        "Parking" -> R.drawable.round_directions_car_24
        "Locker" -> R.drawable.round_lock_24
        "CCTV" -> R.drawable.round_camera_outdoor_24
        else -> {
            R.drawable.gym
        }
    }
    Column(modifier = Modifier.padding(start = 20.dp)
    ) {
        Icon(
            painter = painterResource(id = amenityResId),
            contentDescription = null, // decorative element
            modifier = Modifier.size(30.dp)
        )
        Text(text = amenity)
    }
}

@Composable
fun ShowAmenities(amenities: List<String>) {
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
                items(amenities) { amenity ->
                    ShowAmenity(amenity)
                }
            }
        )
    }
}

@Composable
fun ShowActivities(
    gymId : String,
    activities: List<String>,
    onClickBookSession: (String, String) -> Unit
) {
    Column() {
        Text(
            text = "Book Workout Session",
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
                items(activities) { activity ->
                    ShowActivity(gymId , activity, onClickBookSession = onClickBookSession)
                }
            }
        )
    }
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ShowGymDetails(
    onClickBookSession: (String, String) -> Unit,
    gymDetailsViewModel: GymDetailsViewModel = hiltViewModel()
) {
    val gymUIState by gymDetailsViewModel.gymDetailsUiState.collectAsState()
    val gym = gymUIState.gym
    Column(Modifier.fillMaxSize()) {
        AutoSlidingCarousel(
            itemsCount = gym.imageUrls.size,
            itemContent = { index ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(gym.imageUrls[index])
                        .build(),
                    contentDescription = "Gym Images",
                    contentScale = ContentScale.Crop, // need to check this
                    modifier = Modifier.height(200.dp) // to put it as constant
                )
            },
            autoScroll = false
        )
        ShowGymTitle(gym.name)
        Divider(thickness = 1.dp)
        ShowGymAddress(gym.name, gym.address)
        Divider(thickness = 1.dp)
        ShowActivities(gymId = gym.id,
            activities = gym.activities,
            onClickBookSession=onClickBookSession)
        Divider(thickness = 1.dp)
        ShowTimings(gym.timings)
        Divider(thickness = 1.dp)
        ShowAmenities(gym.amenities)
        Divider(thickness = 1.dp)
    }
}

@Preview(showBackground = true)
@Composable
fun GymDetailsScreenPreview() {
    MyApplicationTheme {
        val gym: Gym = Gym()
        Column() {
//            ShowActivities(gym.activities)
            Divider(thickness = 1.dp)
        }
    }
}

