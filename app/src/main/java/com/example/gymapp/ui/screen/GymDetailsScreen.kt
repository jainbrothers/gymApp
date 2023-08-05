package com.example.gymapp.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
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
import com.example.gymapp.model.Location
import com.example.gymapp.model.PlannedActivitySchedule
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
fun ShowGymAddress(address: Address) {
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
            Icon(
                painter = painterResource(id = R.drawable.round_near_me_24),
                contentDescription = null,
                modifier = Modifier.padding(5.dp)
            )
        }
    }
}

@Composable
fun ShowPlannedActivitySchedules(plannedActivityScheduleList: List<PlannedActivitySchedule>) {
    // Sample data
    var expanded by remember { mutableStateOf(false) }
    val items = listOf(
        Item("Gym Timings", plannedActivityScheduleList),
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
                group.items.forEach { schedule ->
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
fun ShowAmenity(facility: String, facilityResId: Int) {
    Row() {
        Icon(
            painter = painterResource(id = facilityResId),
            contentDescription = null, // decorative element
            modifier = Modifier
                .padding(5.dp)
                .size(30.dp)
        )
        Text(text = facility, modifier = Modifier.padding(9.dp))
    }
}

@Composable
fun ShowAmenities(facilities: List<String>) {
    Column() {
        Text(
            text = "Facilities",
            Modifier.padding(top = 10.dp, bottom = 10.dp, start = 10.dp),
            fontWeight = FontWeight.Bold
        )
        LazyVerticalGrid(
            columns = GridCells.Adaptive(148.dp),

            // content padding
            contentPadding = PaddingValues(
                start = 2.dp,
                top = 6.dp,
                end = 2.dp,
                bottom = 6.dp
            ),
            content = {
                items(facilities) { facility ->
                    val facilityResId: Int = when (facility) {
                        "Parking" -> R.drawable.round_directions_car_24
                        "Locker" -> R.drawable.round_lock_24
                        "CCTV" -> R.drawable.round_camera_outdoor_24
                        else -> {
                            0
                        }
                    }
                    ShowAmenity(facility, facilityResId)
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
    val imgSrcLst = listOf(uiState.gym.imageUrls, uiState.gym.imageUrls)
    Column(Modifier.fillMaxSize()) {
        AutoSlidingCarousel(
            itemsCount = imgSrcLst.size,
            itemContent = { index ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imgSrcLst[index])
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
        ShowGymAddress(uiState.gym.address)
        Divider(thickness = 1.dp)
        ShowPlannedActivitySchedules(uiState.gym.plannedActivitySchedules)
        Divider(thickness = 1.dp)
        ShowAmenities(uiState.gym.amenities)
        Divider(thickness = 1.dp)
    }
}

data class Item(val title: String, val items: List<PlannedActivitySchedule>)


@Preview(showBackground = true)
@Composable
fun GymDetailsScreenPreview() {
    MyApplicationTheme {
//        val mockData =
//            Gym(
//                1,
//                "TAURUS FITNESS",
//                "Gym",
//                "Rajajinagar, No 46, 2nd Floor 10th CrossWest off Chord Road Above Tata motors, Rajajinagar",
//                "This is the Gym Description",
//                imgSrcLst = listOf("https://developer.android.com/codelabs/basic-android-kotlin-compose-amphibians-app/img/great-basin-spadefoot.png")
//            )
//        ShowGymDetailsPage()
        val address = Address(
            1,
            "3rd & 4th floor. 12th Cross Rd",
            "Mahalakshmi Layout",
            "Bengaluru",
            560010,
            Location(
                3.2323,
                4.232323
            )
        )

//        ShowGymAddress(address)
//        Divider()
//        ShowFacilities(listOf("Parking", "Locker"))
//        Divider()
//        ShowFacilities(listOf("Parking", "Locker"))
//        Divider()
    }
}

