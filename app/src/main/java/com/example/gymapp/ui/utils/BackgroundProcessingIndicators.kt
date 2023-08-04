package com.example.gymapp.ui.utils

import androidx.compose.foundation.Image
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.gymapp.R

@Composable
fun Spinner(doShow: Boolean, modifier: Modifier = Modifier) {
    if (doShow) {
        CircularProgressIndicator(modifier)
    }
}

/**
 * The displaying the loading message.
 */
@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading),
        modifier = modifier
    )
}