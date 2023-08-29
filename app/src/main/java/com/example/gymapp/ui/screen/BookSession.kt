package com.example.gymapp.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.LeadingIconTab
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.gymapp.R
import com.example.gymapp.model.SessionSchedule
import com.example.gymapp.ui.screen.enumeration.ErrorCode
import com.example.gymapp.ui.screen.viewmodel.BookSessionViewModel
import com.example.gymapp.ui.screen.viewmodel.state.BookSessionUiState
import com.example.gymapp.ui.screen.viewmodel.state.DaywiseSessionsInfo
import com.example.gymapp.ui.screen.viewmodel.state.SelectedSessionInfo
import com.example.gymapp.ui.theme.MyApplicationTheme
import kotlinx.coroutines.launch

typealias TabContentRenderingFunc = @Composable (Int)->Unit

private const val TAG = "Workout selection tag"
@Composable
fun BookSession(
    modifier: Modifier = Modifier
) {
    val bookSessionViewModel: BookSessionViewModel = hiltViewModel()
    val uiState by bookSessionViewModel.bookSessionUiState.collectAsState()
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        if (uiState.errorCode != ErrorCode.None) {
            ErrorMessage(uiState.errorCode, modifier)
        } else {
            Column (
                modifier = modifier.fillMaxWidth().weight(1f)
            ) {
                GymHighlightMessage(modifier)
                ScheduleSession(bookSessionViewModel, uiState, modifier)
            }
            ProceedButton(uiState, modifier)
        }
    }
}
@Composable
fun GymHighlightMessage(modifier: Modifier = Modifier) {
    Box(modifier = modifier
        .padding(all = 4.dp)
        .fillMaxWidth()
        .background(Color.LightGray)) {
        Text(
            text = stringResource(R.string.book_session_header_text),
            color = Color.Magenta,
            modifier = modifier.padding(10.dp)
        )
    }
}
@Composable
fun ScheduleSession(
    bookSessionViewModel: BookSessionViewModel,
    uiState: BookSessionUiState,
    modifier: Modifier = Modifier)
{
    Column() {
        ScheduleSessionHeader(modifier)
        ScheduleList(bookSessionViewModel, uiState)
    }
}
@Composable
fun ScheduleSessionHeader(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.book_session_screen_header_message),
            fontWeight = FontWeight.Bold,
            modifier = modifier
                .align(Alignment.Center)
                .padding(10.dp)
        )
    }
}
@Composable
fun ScheduleList(
    bookSessionViewModel: BookSessionViewModel,
    uiState: BookSessionUiState,
    modifier: Modifier = Modifier)
{
    Box(modifier = modifier
        .fillMaxWidth()) {
        if (uiState.daywiseSessionsInfo.isNullOrEmpty()) {
            Text(text = "No schedule found for ${uiState.selectedActivity} activity!!!")
            return
        }
        ScheduleListForActivity(
            uiState.daywiseSessionsInfo,
            uiState.selectedSessionInfo,
            {newSessionScheduleInfo -> bookSessionViewModel.setSelectedSessionScheduleIndex(newSessionScheduleInfo)}
        )
    }
}
class DayWiseScheduleTabItem(val title: String, val icon: ImageVector, val renderTabContents: TabContentRenderingFunc)
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleListForActivity(
    dayWiseScheduleList: List<DaywiseSessionsInfo>,
    selectedSessionInfo: SelectedSessionInfo?,
    onSessionSelection: (SelectedSessionInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    val scheduleTabList: MutableList<DayWiseScheduleTabItem> = mutableListOf()
    dayWiseScheduleList.forEach {listItem ->
        scheduleTabList.add(DayWiseScheduleTabItem(listItem.displayName, Icons.Default.Home, {currentTabIndex -> ScheduleListForDay(
            listItem.sessionList,
            selectedSessionInfo,
            currentTabIndex,
            onSessionSelection
        )}))
    }
    val pagerState: PagerState = rememberPagerState()
    Column(modifier = Modifier.fillMaxWidth()) {
        ScheduleTabs(scheduleTabList, pagerState)
        ScheduleTabContent(scheduleTabList, pagerState)
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleTabs(tabs: List<DayWiseScheduleTabItem>, pagerState: PagerState) {
    val scope = rememberCoroutineScope()
    ScrollableTabRow(
        selectedTabIndex = pagerState.currentPage,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        indicator = {tabPositions ->
            Modifier.tabIndicatorOffset(tabPositions.get(pagerState.currentPage))
        }
    ) {
        tabs.forEachIndexed { index, tabItem ->
            LeadingIconTab(
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(index)
                    }
                },
                text = {
                    Text(tabItem.title, softWrap = false)
                },
                icon = {
                    Icon(
                        imageVector = tabItem.icon,
                        contentDescription = null)
                }
            )
        }
    }
}
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ScheduleTabContent(tabs: List<DayWiseScheduleTabItem>, pagerState: PagerState) {
    HorizontalPager(pageCount = tabs.size, state = pagerState) { pageIndex ->
        tabs[pageIndex].renderTabContents(pageIndex)
    }
}
@Composable
fun ScheduleListForDay(
    scheduleList: List<SessionSchedule>?,
    selectedSessionInfo: SelectedSessionInfo?,
    currentTabIndex: Int,
    onSessionSelection: (SelectedSessionInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    if (scheduleList.isNullOrEmpty()) {
        Text(stringResource(R.string.no_schedule_found))
        return
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.padding(4.dp)
    ) {
        items(scheduleList) { item ->
            var backgroundColor = Color.Transparent
            val isItemSelected = (
                    scheduleList.indexOf(item) == selectedSessionInfo?.scheduleIndex &&
                            currentTabIndex == selectedSessionInfo?.scheduleTabIndex)

            if (isItemSelected) {
                backgroundColor = Color.Cyan
            }
            Box(
                modifier = modifier
                    .padding(10.dp)
                    .border(
                        width = 2.dp,
                        color = Color.Blue,
                        shape = RoundedCornerShape(corner = CornerSize(20.dp))
                    )
                    .background(
                        backgroundColor,
                        RoundedCornerShape(corner = CornerSize(20.dp))
                    )
                    .clickable {
                        val index = scheduleList.indexOf(item)
                        onSessionSelection(SelectedSessionInfo(index, currentTabIndex, item))
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${item.beginHour}:${item.beginMinute}-${item.endHour}:${item.endMinute}"
                )
            }
        }
    }
}
@Composable
fun ProceedButton(uiState: BookSessionUiState, modifier: Modifier = Modifier) {
    Column (
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    )
    {
        Button(
            onClick = { /*TODO*/ },
            enabled = uiState.selectedSessionInfo != null
        ) {
            Text(
                text = "Book Session",
                fontWeight = FontWeight.Bold
            )
        }
    }
}
@Composable
fun ErrorMessage(errorCode: ErrorCode, modifier: Modifier = Modifier) {
    Text(
        text = "${errorCode.message}",
        fontWeight = FontWeight.Bold,
        color = Color.Red
    )
}
@Preview(showBackground = true)
@Composable
fun BookSessionPreview() {
    MyApplicationTheme() {
        BookSession()
    }
}