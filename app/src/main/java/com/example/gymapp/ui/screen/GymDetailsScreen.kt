package com.example.gymapp.ui.screen

import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gymapp.R
import com.example.gymapp.model.Address
import com.example.gymapp.model.Gym
import com.example.gymapp.model.Location
import com.example.gymapp.ui.screen.viewmodel.GymDetailsViewModel
import com.example.gymapp.ui.theme.MyApplicationTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.delay

// Icons downloaded from https://fonts.google.com/icons?icon.platform=android

// gym details meaning
// Image
// text details
// etc


@Composable
fun DotsIndicator1(
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
fun AutoSlidingCarousel1(
    modifier: Modifier = Modifier,
    autoSlideDuration: Long = 1000, // AUTO_SLIDE_DURATION,
    pagerState: PagerState = remember { PagerState() },
    itemsCount: Int,
    itemContent: @Composable (index: Int) -> Unit,
) {
    val isDragged by pagerState.interactionSource.collectIsDraggedAsState()
    if (isDragged) {
        LaunchedEffect(pagerState.currentPage) {
            delay(autoSlideDuration)
            pagerState.animateScrollToPage((pagerState.currentPage + 1) % itemsCount)
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
            DotsIndicator1(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                totalDots = itemsCount,
                selectedIndex = if (isDragged) pagerState.currentPage else pagerState.targetPage,
                dotSize = 8.dp
            )
        }
    }
}


@Composable
fun ShowGymTitle(
    modifier: Modifier = Modifier,
    viewModel: GymDetailsViewModel = hiltViewModel(),
) {

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
                    .weight(1F)) {
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
fun ShowGymSchedule() {

}

@Composable
fun ShowFacility(facility: String){
    Row() {
        Icon(
            painter = painterResource(id = R.drawable.round_location_on_24),
            contentDescription = null, // decorative element
            modifier = Modifier
                .padding(5.dp)
                .size(30.dp)
        )
        Text(text = facility)
    }
}


@Composable
fun ShowGymFacilities() {

}


@Composable
fun ShowGymImages(
    modifier: Modifier = Modifier
) {
    AsyncImage(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 2.dp),
        model = ImageRequest.Builder(context = LocalContext.current)
//                .data(imgSrc)
            .data(R.drawable.loading_img)
            .crossfade(true)
            .build(),
        contentDescription = null,
//        error = painterResource(id = R.drawable.ic_broken_image),
        error = painterResource(id = R.drawable.loading_img),
        placeholder = painterResource(id = R.drawable.loading_img)
//        placeholder = painterResource(id = R.drawable.loading_img)
    )
    Text(text = "test text")
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ShowGymDetailsPage(
    gymDetailsViewModel : GymDetailsViewModel = hiltViewModel()
) {
    val uiState by gymDetailsViewModel.gymDetailsUiState.collectAsState()
    val gym = uiState.gym
    val imgSrcLst = listOf(gym.imgSrcLst, gym.imgSrcLst)
    Column(Modifier.fillMaxSize()) {
        AutoSlidingCarousel1(
            itemsCount = imgSrcLst.size,
            itemContent = { index ->
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imgSrcLst[index])
                        .build(),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.height(200.dp)
                )
            }
        )
        ShowGymAddress(gym.address)
    }
}



@Preview(showBackground = true)
@Composable
fun GymDetailsScreenPreview() {
    MyApplicationTheme() {
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

        ShowGymAddress(address)
//        ShowFacility("Parking")
    }
}

