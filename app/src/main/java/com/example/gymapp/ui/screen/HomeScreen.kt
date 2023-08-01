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

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.gymapp.R
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymapp.model.Gym
import com.example.gymapp.ui.screen.viewmodel.AmphibiansUiState
import com.example.gymapp.ui.screen.viewmodel.GymViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gymapp.ui.navigation.NavigationDestination
import com.example.gymapp.ui.screen.enumeration.ScreenName
import com.example.gymapp.ui.theme.MyApplicationTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.delay

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
    Column() {
        when (amphibiansUiState) {
            is AmphibiansUiState.Loading -> LoadingScreen(
                modifier.fillMaxSize().size(200.dp))
            is AmphibiansUiState.Success -> ShowGymList(amphibiansUiState.amphibians,
                modifier = modifier.fillMaxSize(), onItemClick = onItemClick)
            else -> ErrorScreen(retryAction = {}, modifier.fillMaxSize())
        }
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

@Composable
fun IndicatorDot(
    modifier: Modifier = Modifier,
    size: Dp,
    color: Color
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(color)
    )
}

@Composable
fun DotsIndicator(
    modifier: Modifier = Modifier,
    totalDots: Int,
    selectedIndex: Int,
    selectedColor: Color = Color.Yellow /* Color.Yellow IndicatorSelectedColor */,
    unSelectedColor: Color = Color.Gray /* Color.Gray IndicatorSelectedColor */,
    dotSize: Dp
) {
    LazyRow(
        modifier = modifier
            .wrapContentWidth()
            .wrapContentHeight()
    ) {
        items(totalDots) { index ->
            IndicatorDot(
                color = if (index == selectedIndex) selectedColor else unSelectedColor,
                size = dotSize
            )
            if (index != totalDots - 1) {
                Spacer(modifier = Modifier.padding(horizontal = 2.dp))
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AutoSlidingCarousel(
    modifier: Modifier = Modifier,
    autoSlideDuration: Long = 3000, // AUTO_SLIDE_DURATION,
    pagerState: PagerState = remember { PagerState() },
    itemsCount: Int,
    itemContent: @Composable (index: Int) -> Unit,
) {
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    if (isDragged) {
        LaunchedEffect(pagerState.currentPage) {
            delay(autoSlideDuration)
            //pagerState.animateScrollToPage((pagerState.currentPage + 1) % itemsCount)
        }
    }
    Box(
        modifier = modifier.fillMaxWidth(),
    ) {
        HorizontalPager(count = itemsCount, state = pagerState) { page ->
            itemContent(page)
        }

        // you can remove the surface in case you don't want
        // the transparent background
        Surface(
            modifier = Modifier
                .padding(bottom = 8.dp)
                .align(Alignment.BottomCenter),
            shape = CircleShape,
            color = Color.Black.copy(alpha = 0.5f)
        ) {
            DotsIndicator(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                totalDots = itemsCount,
                selectedIndex = if (isDragged) pagerState.currentPage else pagerState.targetPage,
                dotSize = 8.dp
            )
        }
    }
}

// https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#card
@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
@Composable
fun ShowGymCard(
    gym: Gym,
    onItemClick: (Int) -> Unit,
): Unit {
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

        val images = listOf(
            gym.imgsrc,
            "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__340.jpg",
            "https://upload.wikimedia.org/wikipedia/commons/3/33/Vanamo_Logo.png"
        )
            AutoSlidingCarousel(
                itemsCount = images.size,
                itemContent = { index ->
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(images[index])
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.height(200.dp)
                    )
                }
            )
        Column() {
                    Text(text = gym.name)
                    Text(text = "gym.address")
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
        ShowGymList(mockData, modifier = Modifier.fillMaxSize(), onItemClick = {})
    }
}