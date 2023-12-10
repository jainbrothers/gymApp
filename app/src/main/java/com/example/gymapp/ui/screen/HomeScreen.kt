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
import com.example.gymapp.model.GymFullTextSearch
import com.example.gymapp.ui.theme.MyApplicationTheme
import com.example.gymapp.ui.utils.AutoSlidingCarousel
import com.google.accompanist.pager.ExperimentalPagerApi

@Composable
fun HomeScreen(
    onClickGymDetails: (String) -> Unit,
    gymListingViewModel: GymListingViewModel = hiltViewModel()
) {
    val searchText: String = gymListingViewModel.searchText
    Column() {
        GymSearchBox(
            searchText = searchText,
            onSearchTextChange = { gymListingViewModel.updateSearchText(it) }
        )
        ShowGymList(onClickGymCard = onClickGymDetails, gymListingViewModel = gymListingViewModel)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GymSearchBox(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = searchText,
            onValueChange = onSearchTextChange,
            label = { Text(text = "Search Gyms") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}


@Composable
fun ShowGymList(
    modifier: Modifier = Modifier,
    onClickGymCard: (String) -> Unit,
    gymListingViewModel: GymListingViewModel

) {
    val homeScreenUIState = gymListingViewModel.uiState.collectAsState()
    val gymList: List<GymFullTextSearch> = homeScreenUIState.value.gymFullTextSearchList
    Column() {
        LazyColumn(modifier = Modifier, contentPadding = PaddingValues(vertical = 8.dp)) {
            items(items = gymList, key = { gym -> gym.id }) { gym ->
                Log.d("ShowGymList", "printing gym information : $gym")
                ShowGymCard(gymFullTextSearch = gym, onClickGymCard = onClickGymCard)
            }
        }
    }
}

// https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#card
@OptIn(ExperimentalPagerApi::class)
@Composable
fun ShowGymCard(
    gymFullTextSearch: GymFullTextSearch,
    onClickGymCard: (String) -> Unit,
): Unit {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 8.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)
            .clickable { onClickGymCard(gymFullTextSearch.id) },
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
            itemsCount = gymFullTextSearch.imageUrls.size,
            itemContent = { index ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(gymFullTextSearch.imageUrls[index])
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
                text = gymFullTextSearch.name,
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
                    text = gymFullTextSearch.address.locality
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GymListScreenPreview() {
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