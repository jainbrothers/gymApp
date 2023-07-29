package com.example.gymapp.ui.screen

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.with
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.gymapp.R
import com.example.gymapp.ui.screen.viewmodel.GymDetailsViewModel
import com.example.gymapp.ui.theme.MyApplicationTheme
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource

@Composable
fun InfinitelyPulsingHeart() {
    // Creates an [InfiniteTransition] instance for managing child animations.
    val infiniteTransition = rememberInfiniteTransition()

    // Creates a child animation of float type as a part of the [InfiniteTransition].
    val scale by infiniteTransition.animateFloat(
        initialValue = 3f,
        targetValue = 2f,
        animationSpec = infiniteRepeatable(
            // Infinitely repeating a 1000ms tween animation using default easing curve.
            animation = tween(2000),
            // After each iteration of the animation (i.e. every 1000ms), the animation will
            // start again from the [initialValue] defined above.
            // This is the default [RepeatMode]. See [RepeatMode.Reverse] below for an
            // alternative.
            repeatMode = RepeatMode.Restart
        )
    )

    // Creates a Color animation as a part of the [InfiniteTransition].
    val color by infiniteTransition.animateColor(
        initialValue = Color.Red,
        targetValue = Color(0xff800000), // Dark Red
        animationSpec = infiniteRepeatable(
            // Linearly interpolate between initialValue and targetValue every 1000ms.
            animation = tween(1000, easing = LinearEasing),
            // Once [TargetValue] is reached, starts the next iteration in reverse (i.e. from
            // TargetValue to InitialValue). Then again from InitialValue to TargetValue. This
            // [RepeatMode] ensures that the animation value is *always continuous*.
            repeatMode = RepeatMode.Reverse
        )
    )

    Box(Modifier.fillMaxSize()) {
        Icon(
            Icons.Filled.Favorite,
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.Center)
                .graphicsLayer(
                    scaleX = scale,
                    scaleY = scale
                ),
            tint = color
        )
//        ShowGymBanner()
    }
}

// gym details meaning
// Image
// text details
// etc


@Composable
fun ShowGymTitle(
    modifier: Modifier = Modifier,
    viewModel: GymDetailsViewModel = hiltViewModel(),
) {

}

@Composable
fun ShowGymAddress() {

}

@Composable
fun ShowGymSchedule() {

}

@Composable
fun ShowGymFacilities() {

}


@Composable
fun ShowGymBanner(
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 2.dp),
        model = ImageRequest.Builder(context = LocalContext.current)
//                .data(imgSrc)
            .data(R.drawable.great_basin_spade)
            .crossfade(true)
            .build(),
        contentDescription = null,
//        error = painterResource(id = R.drawable.ic_broken_image),
        error = painterResource(id = R.drawable.great_basin_spade),
        placeholder = painterResource(id = R.drawable.great_basin_spade)
//        placeholder = painterResource(id = R.drawable.loading_img)
    )
    Text(text = "test text")
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ShowGymCard1(noImages: Int = 5) {
    Column(Modifier.fillMaxSize()) {
//        var isVisible by remember {
//            mutableStateOf(false)
//        }
//
//        Button(onClick = {
//            isVisible = !isVisible
//        }) {
//            Text(text = "Toggle")
//        }
//
//        val transition = rememberInfiniteTransition()
//        val CustomEasing = Easing { fraction -> (fraction*noImages).toInt()*1f}
//        val infLoop by transition.animateFloat(
//            initialValue = 0f,
//            targetValue = 1f,
//            animationSpec = infiniteRepeatable(
//                repeatMode = RepeatMode.Restart,
//                animation = tween(4000*noImages, easing = CustomEasing)
//            )
//        )


//        AnimatedContent(
//            targetState = infLoop,
//            modifier = Modifier
//                .fillMaxWidth()
//                .weight(1f),
//            content = { infLoop ->
//                Log.e("rahul infLoop","$infLoop")
//                when (infLoop) {
//                    0f -> Image(
//                        painter = painterResource(R.drawable.gym1),
//                        contentDescription = stringResource(R.string.loading)
//                    )
//                    1f -> Image(
//                        painter = painterResource(R.drawable.gym2),
//                        contentDescription = stringResource(R.string.loading),
//                    )
//                    2f -> Image(
//                        painter = painterResource(R.drawable.gym3),
//                        contentDescription = stringResource(R.string.loading),
//                    )
//                    3f -> Image(
//                        painter = painterResource(R.drawable.gym4),
//                        contentDescription = stringResource(R.string.loading),
//                    )
//                    4f -> Image(
//                        painter = painterResource(R.drawable.gym5),
//                        contentDescription = stringResource(R.string.loading),
//                    )
//                    else -> {
//                        // logo image of the company
//                        Image(
//                            painter = painterResource(R.drawable.ic_logo),
//                            contentDescription = stringResource(R.string.loading),
//                        )
//                    }
//                }
////                if (infLoop == 0f) {
////                    Log.e("rahul infLoop","$infLoop")
////                    Image(
////                        painter = painterResource(R.drawable.loading_img),
////                        contentDescription = stringResource(R.string.loading),
////                    )
////                    // Box(modifier = Modifier.background(Color.Green))
////                } else {
////                    Log.e("rahul infLoop","$infLoop")
////                    Image(
////                        painter = painterResource(R.drawable.great_basin_spade),
////                        contentDescription = stringResource(R.string.loading),
////                    )
////                    // Box(modifier = Modifier.background(Color.Red))
////                }
//            },
//            transitionSpec = {
//                slideInHorizontally(
//                    initialOffsetX = { -it },
//                    animationSpec = tween(2000)
//                ) with slideOutHorizontally(
//                    targetOffsetX = { it },
//                    animationSpec = tween(2000)
//                )
//            }
//        )

    }
}

@Preview(showBackground = true)
@Composable
fun GymDetailsScreenPreview() {
    MyApplicationTheme() {
//        ShowGymDetails(mockData, modifier=Modifier.fillMaxSize(), onItemClick = {})
        ShowGymCard1()
    }
}

//val mockData =
//    Gym(
//        1,
//        "TAURUS FITNESS",
//        "Gym",
//        "Rajajinagar, No 46, 2nd Floor 10th CrossWest off Chord Road Above Tata motors, Rajajinagar",
//        "This is the Gym Description",
//        imgsrc = "https://developer.android.com/codelabs/basic-android-kotlin-compose-amphibians-app/img/great-basin-spadefoot.png"
//    )

//AnimatedVisibility(visible = isVisible,
//enter = slideInHorizontally() + fadeIn(),
//modifier = Modifier.fillMaxWidth().weight(5f)
//) {
//    Box(modifier = Modifier.background(Color.Green))
//}


//        val mockData =
//            Gym(
//                1,
//                "TAURUS FITNESS",
//                "Gym",
//                "Rajajinagar, No 46, 2nd Floor 10th CrossWest off Chord Road Above Tata motors, Rajajinagar",
//                "This is the Gym Description",
//                imgsrc = "https://developer.android.com/codelabs/basic-android-kotlin-compose-amphibians-app/img/great-basin-spadefoot.png"
//            )

//        Row(modifier = Modifier.fillMaxWidth()) {
//            var visible by remember { mutableStateOf(true) }
//            Spacer(modifier = Modifier)
//            val density = LocalDensity.current
//            InfinitelyPulsingHeart()
//            AnimatedVisibility(
//                visible = visible,
//                enter = slideInHorizontally {
//                    // Slide in from 40 dp from the top.
//                    with(density) { 80.dp.roundToPx() }
//                } + expandHorizontally (
//                    // Expand from the top.
//                    expandFrom = Alignment.End
//                ) + fadeIn(
//                    // Fade in with the initial alpha of 0.3f.
//                    initialAlpha = 9f
//                ),
//                exit = slideOutHorizontally {
//                    with(density){-80.dp.roundToPx()}
//                }
//                        + shrinkVertically()
//                        + fadeOut()
//            ) {
//                ShowGymBanner(mockData.imgsrc)
//            }
//        }