/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.gymapp.ui.screen

import android.Manifest
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import com.example.gymapp.R
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymapp.ui.screen.viewmodel.GymListingViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gymapp.model.GymFullTextSearchIndex
import com.example.gymapp.ui.theme.MyApplicationTheme
import com.example.gymapp.ui.utils.AutoSlidingCarousel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//fun PreciseLocation(){
//    val context = LocalContext.current
//
//    // When precision is important request both permissions but make sure to handle the case where
//    // the user only grants ACCESS_COARSE_LOCATION
//    val fineLocationPermissionState = rememberMultiplePermissionsState(
//        listOf(
//            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
//        ),
//    )
//
//    // Keeps track of the rationale dialog state, needed when the user requires further rationale
//    var rationaleState by remember {
//        mutableStateOf<RationaleState?>(null)
//    }
//
//    Column() {
//        rationaleState?.run { PermissionRationaleDialog(rationaleState = this) }
//        Log.d("sarkar", "location access")
//
//        if (fineLocationPermissionState.shouldShowRationale) {
//            rationaleState = RationaleState(
//                "Request Precise Location",
//                "In order to use this feature please grant access by accepting " + "the location permission dialog." + "\n\nWould you like to continue?",
//            ) { proceed ->
//                if (proceed) {
//                    fineLocationPermissionState.launchMultiplePermissionRequest()
//                }
//                rationaleState = null
//            }
//        } else {
//            fineLocationPermissionState.launchMultiplePermissionRequest()
//        }
//    }
//}



@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PreciseLocation(){
    val context = LocalContext.current

    // When precision is important request both permissions but make sure to handle the case where
    // the user only grants ACCESS_COARSE_LOCATION
    val fineLocationPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
        ),
    )

    // Keeps track of the rationale dialog state, needed when the user requires further rationale
    var rationaleState by remember {
        mutableStateOf<RationaleState?>(null)
    }

    Column() {
        rationaleState?.run { PermissionRationaleDialog(rationaleState = this) }
        Log.d("sarkar", "location access")

        if (fineLocationPermissionState.shouldShowRationale) {
            rationaleState = RationaleState(
                "Request Precise Location",
                "In order to use this feature please grant access by accepting " + "the location permission dialog." + "\n\nWould you like to continue?",
            ) { proceed ->
                if (proceed) {
                    fineLocationPermissionState.launchMultiplePermissionRequest()
                }
                rationaleState = null
            }
        } else {
            fineLocationPermissionState.launchMultiplePermissionRequest()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBox(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(value = searchText,
            onValueChange = onSearchTextChange,
            label = { Text(text = "Search Gyms")},
            modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun HomeScreen(
    onClickGymDetails: (String) -> Unit,
    gymViewModel: GymListingViewModel = hiltViewModel()
) {
    val searchQuery:String = gymViewModel.searchQuery
    Column() {
        SearchBox(
            searchText = searchQuery,
            onSearchTextChange = {gymViewModel.updateSearchQuery(it)}
        )
        GymListing(onItemClick = onClickGymDetails, gymViewModel = gymViewModel)

    }

}

@Composable
fun GymListing(
    modifier: Modifier = Modifier,
    onItemClick: (String) -> Unit,
    gymViewModel: GymListingViewModel

) {
    val homeScreenUIState = gymViewModel.uiState.collectAsState()
    Column {
        ShowGymList(
            homeScreenUIState.value.gymFullTextSearchIndices,
            modifier = modifier.fillMaxSize(), onItemClick = onItemClick
        )
    }
}

@Composable
fun ShowGymList(
    gymList: List<GymFullTextSearchIndex>,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = Modifier, contentPadding = PaddingValues(vertical = 8.dp)) {
        items(items = gymList, key = { facility -> facility.id }) { item ->
            Log.d("sarkar", "printing gym information : $item")
            ShowGymCard(gym = item, onItemClick = onItemClick)
        }
    }
}

// https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#card
@OptIn(ExperimentalPagerApi::class)
@Composable
fun ShowGymCard(
    gym: GymFullTextSearchIndex,
    onItemClick: (String) -> Unit,
): Unit {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
            .clickable { onItemClick(gym.id) },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor =
            if (isSystemInDarkTheme())
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        AutoSlidingCarousel(
            itemsCount = gym.imageUrls.size,
            itemContent = { index ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(gym.imageUrls[index])
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.height(200.dp),
                    placeholder = painterResource(id = R.drawable.loading_img)
                )
            },
            autoScroll = true
        )
        Column() {
            Text(
                text = gym.name,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 10.dp, top = 10.dp, bottom = 3.dp)
            )
            Row(modifier = Modifier.padding(start = 10.dp, bottom = 10.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.round_location_on_24),
                    contentDescription = "Location", // decorative element
                    modifier = Modifier
                        .padding(end = 5.dp)
                        .size(20.dp)
                )
                Text(
                    text = gym.address.locality
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmphibiansListScreenPreview() {
    MyApplicationTheme() {
//        val mockData = List(10) {
//            Gym(
//                1,
//                "Lorem Ipsum - $it",
//                "$it",
//                "address $it",
//                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do" +
//                        " eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad" +
//                        " minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip" +
//                        " ex ea commodo consequat.",
//                imgSrcLst = listOf("https://developer.android.com/codelabs/basic-android-kotlin-compose-amphibians-app/img/great-basin-spadefoot.png")
//            )
//        }
//        ShowGymList(mockData, modifier = Modifier.fillMaxSize(), onItemClick = {})
    }
}