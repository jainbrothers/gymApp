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
import androidx.compose.ui.res.stringResource
import com.example.gymapp.R
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymapp.model.Gym
import com.example.gymapp.ui.screen.viewmodel.AmphibiansUiState
import com.example.gymapp.ui.screen.viewmodel.GymViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gymapp.ui.navigation.NavigationDestination
import com.example.gymapp.ui.screen.enumeration.ScreenName
import com.example.gymapp.ui.theme.MyApplicationTheme
import com.example.gymapp.ui.utils.AutoSlidingCarousel
import com.example.gymapp.ui.utils.Spinner
import com.google.accompanist.pager.ExperimentalPagerApi

object GymDetailsNav : NavigationDestination {
//    override val route = "gym_details"
    override val route = ScreenName.GYM_DETAILS.name
    override val titleRes = R.string.app_name
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}


@Composable
fun HomeScreen(onClickGymDetails: (Int) -> Unit) {
    GymListApp(onItemClick = onClickGymDetails)
//    ShowGymDetails()
}

@Composable
fun GymListApp(
    modifier: Modifier = Modifier,
    onItemClick: (Int) -> Unit,
    viewModel: GymViewModel = hiltViewModel()
) {
    val amphibiansUiState = viewModel.amphibiansUiState
    Column {
        when (amphibiansUiState) {
            is AmphibiansUiState.Loading ->
                Spinner(
                    doShow = amphibiansUiState is AmphibiansUiState.Loading,
                    modifier = Modifier.padding(200.dp)
                )

//                LoadingScreen(
//                modifier.fillMaxSize().size(200.dp))
            is AmphibiansUiState.Success -> ShowGymList(amphibiansUiState.amphibians,
                modifier = modifier.fillMaxSize(), onItemClick = onItemClick)
            else -> ErrorScreen(retryAction = {}, modifier.fillMaxSize())
        }
    }
}

/**
 * The home screen displaying error message with re-attempt button.
 */
@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.loading_failed))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}


@Composable
fun ShowGymList(
    gymList: List<Gym>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    onScheduleClick: ((String) -> Unit)? = null
) {
    LazyColumn(modifier = Modifier, contentPadding = PaddingValues(vertical = 8.dp)) {
        items(items = gymList, key = { facility -> facility.name }) { item ->
            ShowGymCard(gym = item, onItemClick = onItemClick)
        }
    }
}

// https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#card
@OptIn(ExperimentalPagerApi::class)
@Composable
fun ShowGymCard(
    gym: Gym,
    onItemClick: (Int) -> Unit,
): Unit {
    val imageUrls = listOf(gym.imageUrls, gym.imageUrls)
    Card(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
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
                itemsCount = imageUrls.size,
                itemContent = { index ->
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(imageUrls[index])
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
                    Row() {
                        Icon(
                            painter = painterResource(id = R.drawable.round_location_on_24),
                            contentDescription = "Location", // decorative element
                            modifier = Modifier
                                .padding(5.dp)
                                .size(20.dp)
                        )
                        Text(
                            text = gym.address.locality,
                            modifier = Modifier.padding(start = 10.dp, bottom = 10.dp)
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