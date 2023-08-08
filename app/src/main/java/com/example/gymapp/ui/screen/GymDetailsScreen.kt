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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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




@Composable
fun ShowTimings(plannedActivityScheduleList: List<PlannedActivitySchedule>) {
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
fun ShowAmenity(facility: String) {
        val facilityResId: Int = when (facility) {
            // "dance" -> R.drawable.gym
            // "dance" -> R.drawable.dance_background
            "Parking" -> R.drawable.round_directions_car_24
            "Locker" -> R.drawable.round_lock_24
            "CCTV" -> R.drawable.round_camera_outdoor_24
            else -> {
                0
            }
        }
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
            text = "Amenities",
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
                items(facilities) { facility -> // to be shifted to ShowAmenity
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
            text = "Amenities",
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
                items(workouts) { workout -> // to be shifted to ShowAmenity
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
        // ShowPlannedActivitySchedules(uiState.gym.plannedActivitySchedules) // schedule inside planned activity
        // Divider(thickness = 1.dp)
        ShowAmenities(uiState.gym.amenities)
        Divider(thickness = 1.dp)
        TabbedPages()
    }
}

data class Item(val title: String, val items: List<PlannedActivitySchedule>)


fun ShowSessionDetails(){}

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


// Creating a composable function to
// create a Button and a Bottom Sheet
// Calling this function as content
// in the above function
//@ExperimentalMaterialApi
//@Composable
//fun MyContent(){
//
//    // Declaring a Boolean value to
//    // store bottom sheet collapsed state
//    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState =
//    BottomSheetState(BottomSheetValue.Collapsed))
//
//    // Declaring Coroutine scope
//    val coroutineScope = rememberCoroutineScope()
//
//    // Creating a Bottom Sheet
//    BottomSheetScaffold(
//        scaffoldState = bottomSheetScaffoldState,
//        sheetContent =  {
//            Box(Modifier.fillMaxWidth().height(200.dp).background(Color(0XFF0F9D58))) {
//                Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
//                    Text(text = "Hello Geek!", fontSize = 20.sp, color = Color.White)
//                }
//            }
//        },
//        sheetPeekHeight = 0.dp
//    ){}
//
//    Column(
//        Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
//
//        // Creating a button that changes
//        // bottomSheetScaffoldState value upon click
//        // when bottomSheetScaffoldState is collpased,
//        // it expands and vice-versa
//        Button(onClick = {
//            coroutineScope.launch {
//                if (bottomSheetScaffoldState.bottomSheetState.isCollapsed){
//                    bottomSheetScaffoldState.bottomSheetState.expand()
//                }else{
//                    bottomSheetScaffoldState.bottomSheetState.collapse()
//                }
//            }
//        }, colors = ButtonDefaults.buttonColors(backgroundColor = Color(0XFF0F9D58))) {
//            Text(text = "Click Me", color = Color.White)
//        }
//    }
//}

// For displaying preview in
// the Android Studio IDE emulator
//@ExperimentalMaterialApi
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    MainContent()
//}

//@Composable
//fun BottomSheet(){
//    ModalBottomSheetLayout(
//        sheetContent = {
//            // Add your sheet content here.
//            // This can be any Composable.
//        },
//        sheetState = bottomSheetState
//    ) {
//        // Add your main content here.
//        // This can be any Composable.
//    }
//}

//@Composable
//fun BottomSheetExample() {
//    var expanded by remember { mutableStateOf(false) }
//    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
//        bottomSheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
//    )
//
//    BottomSheetScaffold(
//        scaffoldState = bottomSheetScaffoldState,
//        sheetContent = {
//            // Content for the bottom sheet goes here
//        },
//        sheetPeekHeight = if (expanded) 300.dp else 50.dp,
//        sheetBehavior = if (expanded) BottomSheetBehavior.Expanded else BottomSheetBehavior.PeekHeight,
//        sheetBackgroundColor = MaterialTheme.colors.background
//    ) {
//        // Content for the screen goes here
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CustomBottomSheet() {
//
//    ModalBottomSheet(modifier = Modifier, sheetState = rememberModalBottomSheetState(
//        skipPartiallyExpanded = false), onDismissRequest = {
//    }, shape = RoundedCornerShape(
//        topStart = 10.dp,
//        topEnd = 10.dp
//    ),
//    ) {
//        CustomBottomSheetContainer()
//    }
//}
//
//@Composable
//fun CustomBottomSheetContainer() {
//    Scaffold(topBar = {
//        Column {
//            Text(text = "Theme", modifier = Modifier.height(75.dp)
//                .padding(start = 29.dp, top = 26.dp),fontSize = 23.sp)
//            Divider(color = Color.Black, thickness = 1.dp)
//        }
//    }) {
//        Column(modifier = Modifier.padding(it)) {
//            Text(text = "Select theme", modifier = Modifier
//                .padding(start = 29.dp, top = 20.dp, bottom = 10.dp)
//                .height(40.dp),fontSize = 20.sp)
//            CustomItem("Light")
//            CustomItem("Dark")
//            CustomItem("System default")
//        }
//    }
//}
//
//@Composable
//fun CustomItem(text: String) {
//    Row(modifier = Modifier.height(40.dp), verticalAlignment = Alignment.CenterVertically) {
//        Image(
//            painter = painterResource(id = R.drawable.frame_1),
//            modifier = Modifier.padding(start = 31.dp, top = 9.dp), contentDescription = ""
//        )
//        Text(
//            text = text, modifier = Modifier
//                .height(40.dp)
//                .padding(start = 20.dp, top = 11.dp),
//            fontSize = 18.sp
//        )
//    }
//}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CustomBottomSheet() {
//
//    ModalBottomSheet(modifier = Modifier, sheetState = rememberModalBottomSheetState(
//        skipPartiallyExpanded = false), onDismissRequest = {
//    }, shape = RoundedCornerShape(
//        topStart = 10.dp,
//        topEnd = 10.dp
//    ), dragHandle = null,
//    ) {
//        CustomBottomSheetContainer()
//    }
//}

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
        Column() {
            ShowGymAddress("sdfsdfa", address)
            Divider()
            ShowAmenities(listOf("Parking", "CCTV"))
            Divider()
        }

    }
}

