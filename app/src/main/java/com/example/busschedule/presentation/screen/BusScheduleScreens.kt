package com.example.busschedule.presentation.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.busschedule.R
import com.example.busschedule.commonpresentation.theme.BusScheduleTheme
import com.example.busschedule.domain.model.BusScheduleDomain
import com.example.busschedule.presentation.viewmodel.BusScheduleViewModel
import org.koin.androidx.compose.koinViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class BusScheduleScreens {
    FullSchedule, RouteSchedule
}

@Composable
fun BusScheduleApp(
    viewModel: BusScheduleViewModel = koinViewModel()
) {
    val navController = rememberNavController()
    val fullScheduleTitle = stringResource(R.string.full_schedule)
    var topAppBarTitle by remember { mutableStateOf(fullScheduleTitle) }
    val fullSchedule by viewModel.getFullSchedule().collectAsState(emptyList())
    val onBackHandler = {
        topAppBarTitle = fullScheduleTitle
        navController.navigateUp()
    }

    Scaffold(topBar = {
        BusScheduleTopAppBar(title = topAppBarTitle,
            canNavigateBack = navController.previousBackStackEntry != null,
            onBackClick = { onBackHandler() })
    }) { innerPadding ->
        NavHost(
            navController = navController, startDestination = BusScheduleScreens.FullSchedule.name
        ) {
            composable(BusScheduleScreens.FullSchedule.name) {
                FullScheduleScreen(busSchedule = fullSchedule,
                    contentPadding = innerPadding,
                    onScheduleClick = { busStopName ->
                        navController.navigate(
                            "${BusScheduleScreens.RouteSchedule.name}/$busStopName"
                        )
                        topAppBarTitle = busStopName
                    })
            }
            val busRouteArgument = "busRoute"
            composable(
                route = BusScheduleScreens.RouteSchedule.name + "/{$busRouteArgument}",
                arguments = listOf(navArgument(busRouteArgument) { type = NavType.StringType })
            ) { backStackEntry ->
                val stopName = backStackEntry.arguments?.getString(busRouteArgument)
                    ?: error("busRouteArgument cannot be null")
                val routeSchedule by viewModel.getScheduleFor(stopName).collectAsState(emptyList())
                RouteScheduleScreen(stopName = stopName,
                    busSchedule = routeSchedule,
                    contentPadding = innerPadding,
                    onBack = { onBackHandler() })
            }
        }
    }
}

@Composable
fun FullScheduleScreen(
    busSchedule: List<BusScheduleDomain>,
    onScheduleClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    BusScheduleScreen(
        busSchedule = busSchedule,
        onScheduleClick = onScheduleClick,
        contentPadding = contentPadding,
        modifier = modifier
    )
}

@Composable
fun RouteScheduleScreen(
    stopName: String,
    busSchedule: List<BusScheduleDomain>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onBack: () -> Unit = {}
) {
    BackHandler { onBack() }
    BusScheduleScreen(
        busSchedule = busSchedule,
        modifier = modifier,
        contentPadding = contentPadding,
        stopName = stopName
    )
}

@Composable
fun BusScheduleScreen(
    busSchedule: List<BusScheduleDomain>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    stopName: String? = null,
    onScheduleClick: ((String) -> Unit)? = null,
) {
    val stopNameText = if (stopName == null) {
        stringResource(R.string.stop_name)
    } else {
        "$stopName ${stringResource(R.string.route_stop_name)}"
    }
    val layoutDirection = LocalLayoutDirection.current
    Column(
        modifier = modifier.padding(
            start = contentPadding.calculateStartPadding(layoutDirection),
            end = contentPadding.calculateEndPadding(layoutDirection),
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = contentPadding.calculateTopPadding(),
                    bottom = dimensionResource(R.dimen.padding_medium),
                    start = dimensionResource(R.dimen.padding_medium),
                    end = dimensionResource(R.dimen.padding_medium),
                ), horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stopNameText)
            Text(stringResource(R.string.arrival_time))
        }
        Divider()
        BusScheduleDetails(
            contentPadding = PaddingValues(
                bottom = contentPadding.calculateBottomPadding()
            ), busSchedule = busSchedule, onScheduleClick = onScheduleClick
        )
    }
}

/*
 * Composable for BusScheduleDetails which show list of bus schedule
 * When [onScheduleClick] is null, [stopName] is replaced with placeholder
 * as it is assumed [stopName]s are the same as shown
 * in the list heading display in [BusScheduleScreen]
 */
@Composable
fun BusScheduleDetails(
    busSchedule: List<BusScheduleDomain>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    onScheduleClick: ((String) -> Unit)? = null
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = contentPadding,
    ) {
        items(items = busSchedule, key = { busSchedule -> busSchedule.id }) { schedule ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = onScheduleClick != null) {
                    onScheduleClick?.invoke(schedule.stopName)
                }
                .padding(dimensionResource(R.dimen.padding_medium)),
                horizontalArrangement = Arrangement.SpaceBetween) {
                if (onScheduleClick == null) {
                    Text(
                        text = "--", style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = dimensionResource(R.dimen.font_large).value.sp,
                            fontWeight = FontWeight(300)
                        ), textAlign = TextAlign.Center, modifier = Modifier.weight(1f)
                    )
                } else {
                    Text(
                        text = schedule.stopName, style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = dimensionResource(R.dimen.font_large).value.sp,
                            fontWeight = FontWeight(300)
                        )
                    )
                }
                Text(
                    text = SimpleDateFormat(
                        "h:mm a",
                        Locale.getDefault()
                    ).format(Date(schedule.arrivalTimeInMillis.toLong() * 1000)),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = dimensionResource(R.dimen.font_large).value.sp,
                        fontWeight = FontWeight(600)
                    ),
                    textAlign = TextAlign.End,
                    modifier = Modifier.weight(2f)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BusScheduleTopAppBar(
    title: String, canNavigateBack: Boolean, onBackClick: () -> Unit, modifier: Modifier = Modifier
) {
    if (canNavigateBack) {
        TopAppBar(title = { Text(title) }, navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack, contentDescription = stringResource(
                        R.string.back
                    )
                )
            }
        }, modifier = modifier
        )
    } else {
        TopAppBar(
            title = { Text(title) }, modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun FullScheduleScreenPreview() {
    BusScheduleTheme {
        FullScheduleScreen(busSchedule = List(3) { index ->
            BusScheduleDomain(
                index, "Main Street", 111111
            )
        }, onScheduleClick = {})
    }
}

@Preview(showBackground = true)
@Composable
fun RouteScheduleScreenPreview() {
    BusScheduleTheme {
        RouteScheduleScreen(stopName = "Main Street", busSchedule = List(3) { index ->
            BusScheduleDomain(
                index, "Main Street", 111111
            )
        })
    }
}
