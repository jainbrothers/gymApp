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

package com.example.gymapp.ui.screens.choose

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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.gymapp.R
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gymapp.data.Facility
import androidx.compose.foundation.lazy.items
import com.example.inventory.ui.navigation.NavigationDestination

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

@Composable
fun FacilitySearchApp(
//    navigateToFlightSearch: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = HomeViewModel.factory)
){
    val homeUiState by viewModel.homeUiState.collectAsState()

    Column() {
//        SearchBox(searchQuery = viewModel.searchQuery,
//            onSearchQueryChange = {viewModel.updateSearchQuery(it)},
//            modifier = modifier
//        )
        Spacer(modifier = modifier)
        GymSearchResults(facilityList = homeUiState.facilityList, onItemClick = { })
    }
}

@Composable
fun GymSearchResults(facilityList: List<Facility>,
                     onItemClick: (Int) -> Unit,
                     modifier: Modifier = Modifier,
                     onScheduleClick: ((String) -> Unit)? = null) {
    LazyColumn(modifier = Modifier, contentPadding = PaddingValues(vertical = 8.dp)) {
        items(items = facilityList, key = { facility -> facility.id }) { item ->
            GymDetailCard(facility = item){}
        }
    }
}

// https://developer.android.com/reference/kotlin/androidx/compose/material3/package-summary#card
@Composable
fun GymDetailCard(
    facility: Facility,
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
                Image(
                    // to be replaced with Gym Picture
                    painter = painterResource(id = R.drawable.ic_logo),
                    contentDescription = stringResource(id = R.string.app_name)  // add appropriate content description
                )
                Column() {
                    Text(text = facility.name)
                    Text(text = facility.address)
                    Text(text = "${facility.type} | ${facility.description}")
                }
            }
        }
    }
}