package com.example.gymapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymapp.R
import com.example.gymapp.ui.screen.enumeration.ErrorCode
import com.example.gymapp.ui.screen.viewmodel.BookSessionConfirmationViewModel
import com.example.gymapp.ui.screen.viewmodel.state.BookSessionConfirmationUiState
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun BookSessionConfirmation(
    onClickConfirmButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bookSessionConfirmationViewModel: BookSessionConfirmationViewModel = hiltViewModel()
    val uiState by bookSessionConfirmationViewModel.bookSessionConfirmationUiState.collectAsState()
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        if (uiState.errorCode != ErrorCode.None) {
            RenderErrorMessage(errorCode = uiState.errorCode)
        } else {
            RenderBookSessionDetail(uiState, modifier)
            ConfirmButton(bookSessionConfirmationViewModel, onClickConfirmButton, modifier)
        }
    }
}
@Composable
fun RenderErrorMessage(errorCode: ErrorCode, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        if(errorCode == ErrorCode.DELAYED_LOADING) {
            Text(text = "Loading the page. Please report issue if stuck here for long.")
        } else {
            Text(text = "Unexpected error occured during session booking. ${errorCode.message}")
        }
    }
}

@Composable
fun RenderBookSessionDetail(uiState: BookSessionConfirmationUiState, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        Row(modifier = modifier.padding(10.dp)) {
            Text(text = "You are booking session at: ", fontWeight = FontWeight.Bold)
            Text(text = "${uiState.gym!!.name}")
        }
        Row(modifier = modifier.padding(10.dp)) {
            Text(text = "gym address: ", fontWeight = FontWeight.Bold)
            Column() {
                Text(text = "${uiState.gym!!.address?.streetNameAndNumber}")
                Text(text = "${uiState.gym!!.address?.locality}")
                Text(text = "${uiState.gym!!.address?.city}")
                Text(text = "${uiState.gym!!.address?.pinCode}")
            }
        }

        Row(modifier = modifier.padding(10.dp)) {
            Text(text = "session date and time: ", fontWeight = FontWeight.Bold)
            Text(text = "${Date(uiState.sessionStartEpochInMilli!!) }}")
        }
        Row(modifier = modifier.padding(10.dp)) {
            Text(text = "session duration in minute: ", fontWeight = FontWeight.Bold)
            Text(text = "${uiState.durationInMinute}")
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmButton(
    bookSessionConfirmationViewModel: BookSessionConfirmationViewModel,
    onClickConfirmButton: () -> Unit,
    modifier: Modifier = Modifier
) {
    val snackbarHostState = remember { SnackbarHostState()}
    val confirmationMessage = stringResource(R.string.SESSION_BOOKING_CONFIRMATION_TEXT)
    val scope = rememberCoroutineScope()
    Scaffold(
        snackbarHost = {SnackbarHost(hostState = snackbarHostState)}
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(padding),
            horizontalAlignment = Alignment.CenterHorizontally,
        )
        {
            Button(
                onClick = {
                    scope.launch {
                        bookSessionConfirmationViewModel.createBookedSessionDetail()
                        snackbarHostState.showSnackbar(
                            message = confirmationMessage,
                            duration = SnackbarDuration.Short
                        )
                        onClickConfirmButton()
                    }

                }
            ) {
                Text(
                    text = stringResource(R.string.CONFIRM_BUTTON_TEXT),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}