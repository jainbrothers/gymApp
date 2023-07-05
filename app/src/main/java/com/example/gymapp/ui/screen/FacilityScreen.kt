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

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.gymapp.R
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.gymapp.model.Gym
import com.example.gymapp.ui.screen.viewmodel.AmphibiansUiState
import com.example.gymapp.ui.screen.viewmodel.HomeViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gymapp.ui.theme.MyApplicationTheme

@Composable
fun FacilitySearchApp(
//    navigateToFlightSearch: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.factory)
){
    val amphibiansUiState = viewModel.amphibiansUiState

    Column() {
        Spacer(modifier = modifier)
        HomeScreen(amphibiansUiState = amphibiansUiState, retryAction = { /*TODO*/ })
    }
}

@Composable
fun HomeScreen(
    amphibiansUiState: AmphibiansUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (amphibiansUiState) {
        is AmphibiansUiState.Loading -> LoadingScreen(
            modifier
                .fillMaxSize()
                .size(200.dp))
        is AmphibiansUiState.Success ->
            GymSearchResults(amphibiansUiState.amphibians,
                modifier = modifier.fillMaxSize(), onItemClick = {})
        else -> ErrorScreen(retryAction, modifier.fillMaxSize())
    }
}

/**
 * The home screen displaying the loading message.
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading),
        modifier = modifier
    )
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
fun GymSearchResults(gymList: List<Gym>,
                     onItemClick: (Int) -> Unit,
                     modifier: Modifier = Modifier,
                     onScheduleClick: ((String) -> Unit)? = null) {
    LazyColumn(modifier = Modifier, contentPadding = PaddingValues(vertical = 8.dp)) {
        items(items = gymList, key = { facility -> facility.name }) { item ->
            GymDetailCard(gym = item){}
        }
    }
}

// https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#card
@Composable
fun GymDetailCard(
    gym: Gym,
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    // colors: CardColors = CardDefaults.cardColors(),
    colors: CardColors = CardDefaults.cardColors(),
    elevation: CardElevation = CardDefaults.cardElevation(),
    border: BorderStroke? = null,
    content: @Composable ColumnScope.() -> Unit
): Unit {
    Card(modifier = Modifier
        .fillMaxWidth()
        .height(100.dp)
        .padding(5.dp),
        colors = CardDefaults.cardColors(
        containerColor =
        if (isSystemInDarkTheme())
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.surfaceVariant)
    ) {
        // Card content
        Box(Modifier.fillMaxSize()) {
            Row(Modifier.padding(2.dp)) {
                AsyncImage(
                    modifier = Modifier.size(70.dp,70.dp),
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .data(gym.imgsrc)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    error = painterResource(id = R.drawable.ic_broken_image),
                    placeholder = painterResource(id = R.drawable.loading_img)
                )
                Column() {
                    Text(text = gym.name)
                    Text(text = "facility.address")
                    Text(text = "${gym.type} | ${gym.description}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AmphibiansListScreenPreview() {
    MyApplicationTheme() {
        val mockData = List(10) {
            Gym(
                1,
                "Lorem Ipsum - $it",
                "$it",
                "address $it",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do" +
                        " eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad" +
                        " minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip" +
                        " ex ea commodo consequat.",
                imgsrc = "https://developer.android.com/codelabs/basic-android-kotlin-compose-amphibians-app/img/great-basin-spadefoot.png"
            )
        }
        GymSearchResults(mockData, modifier=Modifier.fillMaxSize(), onItemClick = {})
    }
}